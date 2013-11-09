{-----------------------------------------------------------------------------
    reactive-banana-wx
    
    Example: Counter
------------------------------------------------------------------------------}
{-# LANGUAGE ScopedTypeVariables #-} -- allows "forall t. Moment t"
{-# LANGUAGE RecursiveDo #-}
{-# LANGUAGE Rank2Types #-}

import UDPReceiver
import UDPBroadcaster
import TCPReceiver
import HeartBeatProtocol

import Control.Concurrent

import Control.Monad

import Graphics.UI.WX hiding (Event)
import Reactive.Banana
import Reactive.Banana.WX

import qualified Data.Map as M
import Data.Word
import Network.Info
import System.Time

type ChatMessage = String
type Time = Word64

type Users = M.Map MAC Time

updateUsers :: HeartBeat -> Users -> Users
updateUsers h = M.insertWith max (idHB h) (timestampHB h)

showUser :: (MAC, Time) -> String
showUser (mac, time) = show mac ++ " : " ++ 
                       show (TOD (fromIntegral (time `div` 1000)) 0)

{-----------------------------------------------------------------------------
    Main
------------------------------------------------------------------------------}
main = start $ do
    f       <- frame [text := "Counter"]
    chat    <- singleListBox f []
    clients <- singleListBox f []
    finput  <- entry f [processEnter := True]

    (recvH, recvFire) <- newAddHandler

    set f [layout := margin 10 $ grid 10 10
                [[row 10 [minsize (sz 500 500) (widget chat)
                         ,minsize (sz 400 500) (widget clients)
                         ]]
                ,[expand (widget finput)]
                ]]

    let networkDescription :: forall t. Frameworks t => Moment t ()
        networkDescription = do
        
        eBroadcast <- fromAddHandler recvH
        let eMACs = fmap (show . idHB . fst) eBroadcast
        let eHeartBeats = fst <$> eBroadcast

        eChat <- event0 finput command
        
        emsg <- eventText finput

        let
            (bmsg :: Behavior t String) = stepper "" emsg

            bChatMessages :: Behavior t [ChatMessage]
            bChatMessages = accumB [] $ unions [
                      (:) <$> bmsg <@ eChat
                    , (:) <$> eMACs
             ]

            bUsers :: Behavior t Users
            bUsers = accumB M.empty $ unions [
                    updateUsers <$> eHeartBeats
             ]

        sink chat   [items :== reverse <$> bChatMessages]
        sink clients [items :== (map showUser) <$> M.toList <$> bUsers]

    network <- compile networkDescription    
    actuate network
    forkIO broadcaster
    forkIO $ broadcastReceiver recvFire
    forkIO $ chatReceiver
