module Utils (
    myMAC, currentTimeMillis, hostAddressToString
) where

import Data.Time
import System.Time
import Data.Word
import Control.Applicative
import Network.Info
import Data.List (find)
import Data.Maybe (fromJust)
import Data.Binary
import Control.Monad
import Network.Socket
import qualified Data.ByteString as BS
import qualified Data.ByteString.Char8 as BS8
import Data.Bits
import Data.Char
import Data.List

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

