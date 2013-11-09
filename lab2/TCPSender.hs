module TCPSender (
    chatSender
) where

import ChatMessageProtocol
import Utils

import Control.Monad

import Network.Socket hiding (recvFrom)
import Network.Socket.ByteString
import Control.Applicative ((<$>))

import Data.Binary
import qualified Data.ByteString as BS
import qualified Data.ByteString.Lazy as BSL
import System.IO
import Control.Exception

port = 1236

chatSender :: (SockAddr, ChatMessage) -> IO () -> IO () -> IO ()
chatSender (SockAddrInet _ host, msg) ok fail = send >> ok `catch` handle
 where
    handle :: SomeException -> IO ()
    handle = const fail

    send = do
        addr <- head <$> getAddrInfo Nothing 
                        (Just $ hostAddressToString host)
                        (Just $ show port)
        sock <- socket (addrFamily addr) Stream defaultProtocol
        connect sock (addrAddress addr)
        h <- socketToHandle sock WriteMode
        BS.hPut h $ BSL.toStrict $ encode msg
        hClose h



    
    
