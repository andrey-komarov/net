module HeartBeatProtocol (
    HeartBeat(..),
    currentHeartBeat
) where

import Utils

import Data.Word
import Data.Binary
import Network.Info
import Control.Applicative
import Control.Monad

data HeartBeat = HeartBeat {
    timestampHB :: !Word64,
    idHB :: !MAC
}

instance Binary HeartBeat where
    put (HeartBeat t i) = put t >> put i
    get = HeartBeat <$> get <*> get

instance Binary MAC where
    put (MAC b0 b1 b2 b3 b4 b5) = forM_ [b0, b1, b2, b3, b4, b5] put
    get = MAC <$> get <*> get <*> get <*> get <*> get <*> get

currentHeartBeat :: IO HeartBeat
currentHeartBeat = HeartBeat <$> currentTimeMillis <*> myMAC
