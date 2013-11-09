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
import ChatMessageProtocol
import Utils

import Control.Concurrent

import Control.Monad

import Graphics.UI.WX hiding (Event, on)
import Reactive.Banana
import Reactive.Banana.WX

import qualified Data.Map as M
import qualified Data.ByteString as BS
import Data.Word
import Network.Info
import System.Time
import Network.Socket
import Data.Semigroup
import Data.List
import Data.Function

type Time = Word64

data User = User {
    userLastTime :: !Time,
    userSockAddr :: !SockAddr,
    userNonSentMsgs :: [ChatMessage]
}

instance Semigroup User where
    User t1 a1 m1 <> User t2 a2 m2 = User (max t1 t2) a1 (m1 <> m2)

type Users = M.Map MAC User

updateUsers :: (HeartBeat, SockAddr) -> Users -> Users
updateUsers (h, addr) = M.insertWith (<>) (idHB h) (User (timestampHB h) addr [])

showUser :: (MAC, User) -> [String]
showUser (mac, u) = [
                show mac ++ " : " ++ (ppTime (userLastTime u))] ++
                    [ (show . message) msg | msg <- userNonSentMsgs u]

{-----------------------------------------------------------------------------
    Main
------------------------------------------------------------------------------}
main = start $ do
    f       <- frame [text := "Counter"]
    chat    <- singleListBox f []
    clients <- singleListBox f []
    finput  <- entry f [processEnter := True]

    (recvH, recvFire) <- newAddHandler
    (msgIn :: AddHandler (ChatMessage, SockAddr), msgFire) <- newAddHandler

    set f [layout := margin 10 $ grid 10 10
                [[row 10 [minsize (sz 500 500) (widget chat)
                         ,minsize (sz 400 500) (widget clients)
                         ]]
                ,[expand (widget finput)]
                ]]

    let networkDescription :: forall t. Frameworks t => Moment t ()
        networkDescription = do
        
        eBroadcast <- fromAddHandler recvH
        ePost <- fromAddHandler msgIn
        let eMACs = fmap (show . idHB . fst) eBroadcast

        eChat <- event0 finput command
        
        emsg <- eventText finput

        let
            (bmsg :: Behavior t String) = stepper "" emsg

            bAllMessages :: Behavior t [ChatMessage]
            bAllMessages = accumB [] $ unions [
                (:) <$> fmap fst ePost
             ]

            bChat :: Behavior t [String]
            bChat = reverse <$> map show <$> sortBy (compare `on` timestampCM) 
                            <$> bAllMessages

            bUsers :: Behavior t Users
            bUsers = accumB M.empty $ unions [
                    updateUsers <$> eBroadcast
             ]

        sink chat [items :== bChat]
        sink clients [items :== reverse <$> (concatMap showUser) <$> M.toList <$> bUsers]

    network <- compile networkDescription    
    actuate network
    forkIO broadcaster
    forkIO $ broadcastReceiver recvFire
    forkIO $ chatReceiver msgFire
