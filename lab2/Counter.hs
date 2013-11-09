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

third :: (a, b, c) -> c
third (_, _, c) = c

snd3 :: (a, b, c) -> b
snd3 (_, b, _) = b

fst3 :: (a, b, c) -> a
fst3 (a, _, _) = a

{-----------------------------------------------------------------------------
    Main
------------------------------------------------------------------------------}
main = start $ do
    f        <- frame [text := "Counter"]
    wChat    <- singleListBox f []
    wClients <- singleListBox f []
    wInput   <- entry f [processEnter := True]

    (hCantSend, fireCantSend) <- newAddHandler
    (hNewUDP,   fireNewUDP)   <- newAddHandler
    (hNewTCP,   fireNewTCP)   <- newAddHandler

    set f [layout := margin 10 $ grid 10 10
                [[row 10 [minsize (sz 500 500) (widget wChat)
                         ,minsize (sz 400 500) (widget wClients)
                         ]]
                ,[expand (widget wInput)]
                ]]

    let networkDescription :: forall t. Frameworks t => Moment t ()
        networkDescription = do

        
        eNewUDP :: Event t (HeartBeat, SockAddr, Time) <- fromAddHandler hNewUDP
        eNewTCP   <- fromAddHandler hNewTCP
        eCantSend <- fromAddHandler hCantSend

        eSendMessage <- event0 wInput command

        eCurrentMessageText <- eventText wInput

        let
            bCurrentMessageText :: Behavior t String
            bCurrentMessageText = stepper "" eCurrentMessageText

            eMySentMessages :: Event t String
            eMySentMessages = bCurrentMessageText <@ eCurrentMessageText

            bAllMySentMessages :: Behavior t [String]
            bAllMySentMessages = accumB [] $ (:) <$> eMySentMessages

            bUserBeenSeenLastTime :: Behavior t (M.Map MAC Time)
            bUserBeenSeenLastTime = accumB M.empty $ (\(hb, sa, t) -> 
                M.insertWith max (idHB hb) t) <$> eNewUDP

            bClientsItems :: Behavior t [String]
            bClientsItems = map (\(mac, time) -> show mac ++ " : " ++ ppTime time) <$> 
                ((M.toList <$> bUserBeenSeenLastTime) :: Behavior t [(MAC, Time)])

        --sink chat [items :== bChat]
        sink wClients [items :== bClientsItems]

    network <- compile networkDescription    
    actuate network
    forkIO broadcaster
    forkIO $ broadcastReceiver fireNewUDP
    --forkIO $ chatReceiver msgFire
