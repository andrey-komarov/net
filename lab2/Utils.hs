module Utils (
    myMAC, currentTimeMillis
) where

import Data.Time
import System.Time
import Data.Word
import Control.Applicative
import Network.Info
import Data.List (find)
import Data.Maybe (fromJust)

currentTimeMillis :: IO Word64
currentTimeMillis = (\(TOD t _) -> fromIntegral t) <$> getClockTime  

myMAC :: IO MAC
myMAC = mac <$> fromJust <$> find (("wlp3s0" ==) . name) <$> getNetworkInterfaces
