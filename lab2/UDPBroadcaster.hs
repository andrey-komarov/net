module UDPBroadcaster (
    broadcaster
) where

import HeartBeatProtocol

import Control.Applicative ((<$>))
import Control.Monad (forever)
import Data.Binary
import Control.Concurrent
import Network.Socket
import Network.Socket.ByteString
import qualified Data.ByteString.Lazy as BSL

import Utils
import System.Time
timeout = 5000000
port = 1235

broadcastAddress = head <$> getAddrInfo Nothing (Just "255.255.255.255") (Just $ show port)

broadcast :: Binary b => Socket -> b -> IO ()
broadcast s d = do
    a <- broadcastAddress
    sendAllTo s (BSL.toStrict $ encode d) (addrAddress a)

initSocket :: IO Socket
initSocket = do
    a <- broadcastAddress
    sock <- socket (addrFamily a) Datagram defaultProtocol
    setSocketOption sock Broadcast 1
    return sock

broadcaster :: IO ()
broadcaster = do
    sock <- initSocket
    forever $ do
        currentHeartBeat >>= broadcast sock
        t <- currentTimeMillis
        threadDelay timeout 
