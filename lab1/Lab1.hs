{-# LANGUAGE OverloadedStrings #-}
import Network.Socket hiding (sendTo, recvFrom)
import Network.Socket.ByteString
import qualified Data.ByteString as BS
import qualified Data.ByteString.Lazy as BSL

import Data.Time
import System.Time
import Data.Binary
import Data.Binary.Put
import Data.Binary.Get
import Data.Word

import Control.Concurrent
import Control.Applicative

port = 44444
sendTimeout = 1000000
killTimeout = 15000000

hostnameLength = 20
usernameLength = 20

data Msg = Msg 
    { msgHostFrom :: HostAddress
    , msgHostname :: BS.ByteString
    , msgTimestamp :: Word64
    , msgUser :: BS.ByteString
    }


expandTo :: BS.ByteString -> Int -> BS.ByteString
expandTo s n = let len = BS.length s in 
    if len > n 
    then error ("|" ++ show s ++ "| > " ++ show n)
    else s `BS.append` BS.replicate (n - len) 0

unZero :: BS.ByteString -> BS.ByteString
unZero = BS.takeWhile (/= 0)

instance Binary Msg where
    put (Msg host hostname time user) = do
        put host
        putByteString $ hostname `expandTo` hostnameLength
        put time
        putByteString $ user `expandTo` usernameLength
    
    get = do
        host <- get 
        hostname <- unZero <$> getByteString hostnameLength
        time <- get
        user <- unZero <$> getByteString usernameLength
        return $ Msg host hostname time user

instance Show Msg where
    show (Msg host hostname time user) = unwords 
        [show host, show hostname, (show $ TOD (fromIntegral time) 0), show user]

currentUnixTime64 :: IO Word64
currentUnixTime64 = do
    TOD t _ <- getClockTime
    return $ fromIntegral t

mkMsg :: IO Msg
mkMsg = do
    t <- currentUnixTime64
    return $ Msg 0 "melchior" t "komarov"

server :: IO ()
server = do
    addr <- head <$> getAddrInfo
        (Just (defaultHints {addrFlags = [AI_PASSIVE]}))
        Nothing (Just $ show port)
    sock <- socket (addrFamily addr) Datagram defaultProtocol
    bind sock (addrAddress addr)
    loop sock
 where
    loop sock = do
        (t, addr) <- recvFrom sock 100
        (print :: Msg -> IO ()) $ decode $ BSL.fromStrict t
        loop sock
        

client :: IO ()
client = do 
    addr <- head <$> getAddrInfo Nothing (Just "255.255.255.255") (Just "44444")
    sock <- socket (addrFamily addr) Datagram defaultProtocol
    setSocketOption sock Broadcast 1
    loop $ \msg -> sendAllTo sock msg $ addrAddress addr
 where 
    loop :: (BS.ByteString -> IO ()) -> IO ()
    loop send = do
        msg <- mkMsg
        send $ BSL.toStrict $ encode msg
        threadDelay sendTimeout
        loop send


main = do
    forkIO server
    client
