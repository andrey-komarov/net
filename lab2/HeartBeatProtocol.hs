module HeartBeatProtocol (
    HeartBeat(..),
    currentHeartBeat
) where

import Utils

import Data.Word
import Data.Binary
import Data.Binary.Get
import Data.Binary.Put
import Network.Info
import Control.Applicative

data HeartBeat = HeartBeat {
    timestampHB :: !Word64,
    idHB :: !MAC
}

instance Binary HeartBeat where
    put (HeartBeat t i) = putWord64le t >> put i
    get = HeartBeat <$> getWord64le <*> get

currentHeartBeat :: IO HeartBeat
currentHeartBeat = HeartBeat <$> currentTimeMillis <*> myMAC
