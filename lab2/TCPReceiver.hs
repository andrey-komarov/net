module TCPReceiver (
    chatReceiver
) where

import Data

import Control.Monad

import Network.Socket hiding (recvFrom)
import Network.Socket.ByteString
import Control.Applicative ((<$>))

import qualified Data.ByteString as BS
import qualified Data.ByteString.Lazy as BSL
import System.IO
import Control.Exception
import Data.Binary
import Control.Concurrent

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

chatReceiver :: Chan Event -> IO ()
chatReceiver e = do
    sock <- initSocket
    forever $ do
        (do
            (s, addr) <- accept sock
            h <- socketToHandle s ReadMode
            t <- BSL.hGetContents h
            writeChan e $ RecvMessage $ decode t)

