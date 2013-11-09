module ChatMessageProtocol (
    ChatMessage(..)
) where

import Utils

import Data.Word
import Data.Binary
import Data.Binary.Get
import Data.Binary.Put
import Network.Info
import Control.Applicative
import qualified Data.ByteString.UTF8 as BSU
import qualified Data.ByteString as BS

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
        putWord64le time
        put id
        putWord32le len
        putByteString msg
    get = do
        time <- getWord64le
        id <- get
        len <- getWord32le
        msg <- getByteString (fromIntegral len)
        return $ ChatMessage time id len msg

