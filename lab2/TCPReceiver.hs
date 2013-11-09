module TCPReceiver (
    chatReceiver
) where

import ChatMessageProtocol

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

chatReceiver :: ((ChatMessage, SockAddr) -> IO ()) -> IO ()
chatReceiver callback = do
    sock <- initSocket
    forever $ do
        (do
            print "accepting"
            (s, addr) <- accept sock
            print "accepted, transforming to handle"
            h <- socketToHandle s ReadMode
            print "transformtd, parsing data"
            t <- BSL.hGetContents h
            print "closed"
            forkIO ((callback (decode t, addr))  )>> return() ) `catch` handler
 where 
    handler :: SomeException -> IO ()
    handler e = undefined
