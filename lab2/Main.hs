import Data

import UDPReceiver
import UDPBroadcaster

import TCPReceiver

import Control.Monad
import Control.Concurrent

printer :: Show a => Chan a -> IO ()
printer e = do
  ex <- getChanContents e
  forM_ ex print

main :: IO ()
main = do
  s <- newEmptyChatState
  h <- dupChan (heartbeats s)
  m <- dupChan (messages s)
  forkIO $ printer h
  forkIO $ broadcaster
  forkIO $ broadcastReceiver (events s)
  forkIO $ chatReceiver (events s)
  forkIO $ printer m
  threadDelay 1000000000
  print 123
--    forkIO $ broadcastReceiver $ \(h, s) -> print (idHB h)
--    broadcaster
