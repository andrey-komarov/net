import Data

import UDPReceiver
import UDPBroadcaster

import TCPReceiver

import Control.Monad
import Control.Concurrent

import qualified Data.ByteString.Char8 as BSC8

printer :: Show a => Chan a -> IO ()
printer e = do
  ex <- getChanContents e
  forM_ ex print

reader :: Chan ChatMessage -> IO ()
reader msgs = forever $ do
  s <- BSC8.getLine
  msg <- newMessage s
  writeChan msgs msg

main :: IO ()
main = do
  s' <- newEmptyChatState
  s <- newMVar s'
  h <- dupChan (heartbeats s')
  m <- dupChan (messages s')
--  forkIO $ printer h
  forkIO $ broadcaster
  forkIO $ broadcastReceiver s
  forkIO $ chatReceiver (events s')
  forkIO $ printer m
  forkIO $ reader (outgoingMessages s')
  threadDelay 1000000000
  print 123
--    forkIO $ broadcastReceiver $ \(h, s) -> print (idHB h)
--    broadcaster
