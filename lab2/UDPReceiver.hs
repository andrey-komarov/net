module UDPReceiver (
    broadcastReceiver
) where

import HeartBeatProtocol

import Network.Socket hiding (recvFrom)
import Network.Socket.ByteString
import qualified Data.ByteString as BS
import qualified Data.ByteString.Lazy as BSL
import Control.Monad
import Control.Applicative ((<$>))
import Data.Binary
import Control.Concurrent

port = 1235

initSocket :: IO Socket
initSocket = do
    addr <- head <$> getAddrInfo
        (Just (defaultHints {addrFlags = [AI_PASSIVE]}))
        Nothing (Just $ show port)
    sock <- socket (addrFamily addr) Datagram defaultProtocol
    bind sock (addrAddress addr)
    return sock

broadcastReceiver :: ((HeartBeat, SockAddr) -> IO ()) -> IO ()
broadcastReceiver callback = do
    sock <- initSocket
    forever $ do
        (t, addr) <- recvFrom sock 14
        when (BS.length t == 14) $ 
            (forkIO $ callback (decode (BSL.fromStrict t), addr)) >> return ()
