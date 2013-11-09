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
import qualified Data.ByteString as BS
import qualified Data.ByteString.UTF8 as BSU
import Data.Word
import Network.Info
import System.Time
import Network.Socket
import Data.Semigroup

type ChatMessage = String
type Time = Word64

data Message = Message {
    messageText :: BSU.ByteString,
    messageTime :: Time
}

data User = User {
    userLastTime :: !Time,
    userSockAddr :: !SockAddr,
    userNonSentMsgs :: [Message]
}

instance Semigroup User where
    User t1 a1 m1 <> User t2 a2 m2 = User (max t1 t2) a1 (m1 <> m2)

type Users = M.Map MAC User

updateUsers :: (HeartBeat, SockAddr) -> Users -> Users
updateUsers (h, addr) = M.insertWith (<>) (idHB h) (User (timestampHB h) addr [])

showUser :: (MAC, User) -> [String]
showUser (mac, u) = [
                show mac ++ " : " ++ 
                show (TOD (fromIntegral ((userLastTime u) `div` 1000)) 0)] ++
                    [ (show . messageText) msg | msg <- userNonSentMsgs u]

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
                    updateUsers <$> eBroadcast
             ]

        sink chat   [items :== reverse <$> bChatMessages]
        sink clients [items :== reverse <$> (concatMap showUser) <$> M.toList <$> bUsers]

    network <- compile networkDescription    
    actuate network
    forkIO broadcaster
    forkIO $ broadcastReceiver recvFire
    forkIO $ chatReceiver undefined
