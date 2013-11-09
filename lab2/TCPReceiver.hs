module TCPReceiver (
    chatReceiver
) where

import Control.Monad

import Network.Socket hiding (recvFrom)
import Network.Socket.ByteString
import Control.Applicative ((<$>))

import qualified Data.ByteString as BS
import System.IO

port = 1236

initSocket :: IO Socket
initSocket = do
    addr <- head <$> getAddrInfo
        (Just (defaultHints {addrFlags = [AI_PASSIVE]}))
        Nothing (Just $ show port)
    sock <- socket (addrFamily addr) Stream defaultProtocol
    bind sock (addrAddress addr)
    listen sock 10
    return sock

chatReceiver :: IO ()
chatReceiver = do
    sock <- initSocket
    forever $ do
        (s, addr) <- accept sock
        h <- socketToHandle s ReadMode
        t <- BS.hGetContents h
        hClose h
        print t
