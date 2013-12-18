module UDPReceiver (
    broadcastReceiver
) where

import Data
import TCPSender

import Network.Socket hiding (recvFrom)
import Network.Socket.ByteString
import qualified Data.ByteString as BS
import qualified Data.ByteString.Lazy as BSL
import Control.Monad
import Control.Applicative ((<$>))
import Data.Binary
import Control.Concurrent

import qualified Data.Map as M


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

killer :: ThreadId -> IO ()
killer tid = do
  threadDelay 60000000
  killThread tid

accesser :: MVar () -> IO ()
accesser lock = forever $ putMVar lock ()

broadcastReceiver :: MVar ChatState -> IO ()
broadcastReceiver state = do
  st <- readMVar state
  let e = events st
  sock <- initSocket
  forever $ do
    st <- takeMVar state
    (t, addr) <- recvFrom sock 14
    if ((BS.length t) == 14) then (do
      let msg@(HeartBeat tm mac) = decode (BSL.fromStrict t)
      writeChan e $ RecvHeartBeat msg
      case (mac `M.lookup` (ableToSentTo st)) of
        Nothing -> do
          lock <- newMVar ()
          forkIO $ accesser lock
          o' <- dupChan (outgoingMessages st)
          forkIO $ chatSender addr lock o'
          putMVar state $ st { ableToSentTo = M.insert mac lock (ableToSentTo st)}
        Just lock ->
          putMVar state st
      ) else
        putMVar state st


