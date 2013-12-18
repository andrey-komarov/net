module UDPReceiver (
    broadcastReceiver
) where

import Data

import Network.Socket hiding (recvFrom)
import Network.Socket.ByteString
import qualified Data.ByteString as BS
import qualified Data.ByteString.Lazy as BSL
import Control.Monad
import Control.Applicative ((<$>))
import Data.Binary
import Control.Concurrent

type Time = Word64

port = 1235

initSocket :: IO Socket
initSocket = do
    addr <- head <$> getAddrInfo
        (Just (defaultHints {addrFlags = [AI_PASSIVE]}))
        Nothing (Just $ show port)
    sock <- socket (addrFamily addr) Datagram defaultProtocol
    bind sock (addrAddress addr)
    return sock

broadcastReceiver :: Chan Event -> IO ()
broadcastReceiver e = do
    sock <- initSocket
    forever $ do
        (t, addr) <- recvFrom sock 14
        when (BS.length t == 14) $ do
          writeChan e $ RecvHeartBeat $ decode (BSL.fromStrict t)

