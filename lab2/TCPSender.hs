module TCPSender (
    chatSender
) where

import Data

import Control.Monad

import Network.Socket hiding (recvFrom)
import Network.Socket.ByteString
import Control.Applicative ((<$>))
import Control.Concurrent

import Data.Binary
import qualified Data.ByteString as BS
import qualified Data.ByteString.Lazy as BSL
import System.IO
import Control.Exception

port = 1236

setPort :: SockAddr -> SockAddr
setPort (SockAddrInet _ a) = SockAddrInet (PortNum 54276) a

sendMsg :: Binary b => SockAddr -> b -> IO ()
sendMsg sockAddr msg = do
  sock <- socket AF_INET Stream defaultProtocol
  connect sock sockAddr
  h <- socketToHandle sock WriteMode
  BS.hPut h $ BSL.toStrict $ encode msg

chatSender :: SockAddr -> MVar () -> Chan ChatMessage -> IO ()
chatSender sock lock messages = forever $ do
  () <- takeMVar lock
  msg <- readChan messages
  let handler = (\e -> writeChan messages msg) :: IOException -> IO ()
  sendMsg (setPort sock) msg `catch` handler
  
