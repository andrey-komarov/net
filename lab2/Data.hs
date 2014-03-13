module Data where

import System.Time
import Data.Bits
import Data.Maybe
import Data.Word
import Data.List
import Data.Binary
import Data.Binary.Get
import Data.Binary.Put
import Network.Info
import Network.Socket
import Control.Applicative
import Control.Concurrent
import Control.Monad
import qualified Data.ByteString.UTF8 as BSU
import qualified Data.ByteString as BS

import Data.Map (Map)
import qualified Data.Map as M

data HeartBeat = HeartBeat {
    timestampHB :: !Word64,
    idHB :: !MAC
} deriving (Show)

instance Binary HeartBeat where
    put (HeartBeat t i) = putWord64be t >> put i
    get = HeartBeat <$> getWord64be <*> get

currentHeartBeat :: IO HeartBeat
currentHeartBeat = HeartBeat <$> currentTimeMillis <*> myMAC

data ChatMessage = ChatMessage {
    timestampCM :: !Word64,
    idCM :: !MAC,
    messageLen :: !Word32,
    message :: BS.ByteString
}

instance Show ChatMessage where
    show (ChatMessage t mac len msg) = show mac ++ "(at " ++ ppTime t ++ "): " 
        ++ BSU.toString msg

instance Binary ChatMessage where
    put (ChatMessage time id len msg) = do
        putWord64be time
        put id
        putWord32be len
        putByteString msg
    get = do
        time <- getWord64be
        id <- get
        len <- getWord32be
        msg <- getByteString (fromIntegral len)
        return $ ChatMessage time id len msg

newMessage :: BS.ByteString -> IO ChatMessage
newMessage s = do
  t <- currentTimeMillis
  mac <- myMAC
  let len = fromIntegral (BS.length s)
  return $ ChatMessage t mac len s

data Event =
  RecvHeartBeat HeartBeat
  | RecvMessage ChatMessage
    deriving (Show)

isHeartBeat :: Event -> Maybe HeartBeat
isHeartBeat (RecvHeartBeat h) = Just h
isHeartBeat _ = Nothing

isMessage :: Event -> Maybe ChatMessage
isMessage (RecvMessage m) = Just m
isMessage _ = Nothing

             
data ChatState = ChatState {
  ableToSentTo :: Map MAC (MVar ())
  , workerTID :: Map MAC (MVar ThreadId)
  , killerTID :: Map MAC (MVar ThreadId)
  , messages :: Chan ChatMessage
  , heartbeats :: Chan HeartBeat
  , events :: Chan Event
  , outgoingMessages :: Chan ChatMessage
  }

chanFilter :: (a -> Maybe b) -> Chan a -> IO (Chan b)
chanFilter f a = do
  a' <- dupChan a
  b <- newChan
  forkIO $ forever $ do
    aa <- readChan a'
    case f aa of
      Just bb -> writeChan b bb
      Nothing -> return ()
  return b

newEmptyChatState :: IO ChatState
newEmptyChatState = do
  e <- newChan
  h <- chanFilter isHeartBeat e
  m <- chanFilter isMessage e
  o <- newChan
  return $ ChatState M.empty M.empty M.empty m h e o

type Time = Word64

ppTime :: Time -> String
ppTime t = show (TOD (fromIntegral (t `div` 1000)) 0)

currentTimeMillis :: IO Word64
currentTimeMillis = (\(TOD t _) -> fromIntegral t * 1000) <$> getClockTime  

myMAC :: IO MAC
myMAC = mac <$> fromJust <$> find (("wlp3s0" ==) . name) <$> getNetworkInterfaces

hostAddressToString :: HostAddress -> String
hostAddressToString a = intercalate "." $ 
    map (\sh -> show $ (a `shiftR` sh) .&. 255) [24, 16, 8, 0]

instance Binary MAC where
    put (MAC b0 b1 b2 b3 b4 b5) = forM_ [b0, b1, b2, b3, b4, b5] put
    get = MAC <$> get <*> get <*> get <*> get <*> get <*> get

