{-# LANGUAGE OverloadedStrings #-}
import Network.Socket hiding (sendTo, recvFrom)
import Network.Socket.ByteString
import System.Posix.User
import Network.HostName
import qualified Data.ByteString as BS
import qualified Data.ByteString.Lazy as BSL
import Data.ByteString.Char8 (pack)
import qualified Data.ByteString.Char8 as BS8

import Data.Time
import System.Time
import Data.Binary
import Data.Binary.Put
import Data.Binary.Get
import Data.Monoid
import Data.Word
import Data.Function
import Data.List
import Data.Bits

import Text.Printf

import Control.Concurrent
import Control.Applicative

import qualified Data.Map as M

port = 1234
sendTimeout = 1000000
killTimeout = 15000000
printTimeout = 1000000

hostnameLength = 20
usernameLength = 20

data Messages = Messages
    { messagesCounts :: M.Map Msg Int
    , messagesKillerHandler :: M.Map Msg ThreadId
    , myHost :: HostAddress
    }

instance Show Messages where
    show (Messages c _ _) = let s = sortBy (compare `on` snd) (M.toList c) 
                                pp (msg, cnt) = (printf "%5d : %s" cnt $ show msg :: String)
                                len = maximum $ 5: (map (length . pp) $ M.toList c)
                            in unlines $ [replicate len '='] ++ map pp (M.toList c) 
                                ++ [replicate len '=']

emptyMessages :: HostAddress -> Messages
emptyMessages = Messages M.empty M.empty 

removeFromMessages :: Msg -> MVar Messages -> IO ()
removeFromMessages msg state = modifyMVar_ state $ 
    \(Messages c h n) -> return $ Messages (M.delete msg c) (M.delete msg h) n

data Msg = Msg 
    { msgHostFrom :: HostAddress
    , msgHostname :: BS.ByteString
    , msgTimestamp :: Word64
    , msgUser :: BS.ByteString
    }

instance Eq Msg where
    Msg h1 _ _ u1 == Msg h2 _ _ u2 = h1 == h2 && u1 == u2

instance Ord Msg where
    Msg h1 _ _ u1 `compare` Msg h2 _ _ u2 = h1 `compare` h2 <> u1 `compare` u2

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
        [show (hostAddressToString host), show hostname, (show $ TOD (fromIntegral time) 0), show user]

currentUnixTime64 :: IO Word64
currentUnixTime64 = do
    TOD t _ <- getClockTime
    return $ fromIntegral t

myAddr :: MVar Messages -> IO HostAddress
myAddr m = do
    Messages _ _ a <- readMVar m
    return a

mkMsg :: MVar Messages -> IO Msg
mkMsg m = Msg <$> myAddr m <*> (pack <$> getLoginName) <*> currentUnixTime64 <*> (pack <$> getHostName)

infoPrinter :: MVar Messages -> IO ()
infoPrinter state = do
    a <- readMVar state
    print a
    threadDelay printTimeout
    infoPrinter state

hostFromSockAddr (SockAddrInet _ a) = a

hostAddressToString :: HostAddress -> BS.ByteString
hostAddressToString a = BS.intercalate (BS.pack [46]) $ 
    map (\sh -> BS8.pack $ show $ (a `shiftR` sh) .&. 255) [0, 8, 16, 24]

server :: MVar Messages -> IO ()
server state = do
    addr <- head <$> getAddrInfo
        (Just (defaultHints {addrFlags = [AI_PASSIVE]}))
        Nothing (Just $ show port)
    sock <- socket (addrFamily addr) Datagram defaultProtocol
    bind sock (addrAddress addr)
    loop state sock
 where
    loop state sock = do
        (t, addr) <- recvFrom sock 100
        let msg = decodeOrFail $ BSL.fromStrict t
        case msg of 
            Left _ -> putStrLn $ "Can't parse: " ++ show msg
            Right (_, _, msg) -> modifyMVar_ state $ \(Messages counts handlers a) -> do
                let newCounts = M.insertWith (+) msg 1 counts
                case M.lookup msg handlers of
                    Just h -> killThread h
                    Nothing -> return ()
                tid <- forkIO $ do
                    threadDelay killTimeout
                    removeFromMessages msg state
                let newHandlers = M.insert msg tid handlers
                return $ Messages newCounts newHandlers a
        loop state sock
        

client :: MVar Messages -> IO ()
client m = do 
    addr <- head <$> getAddrInfo Nothing (Just "255.255.255.255") (Just $ show port)
    sock <- socket (addrFamily addr) Datagram defaultProtocol
    setSocketOption sock Broadcast 1
    loop $ \msg -> sendAllTo sock msg $ addrAddress addr
 where 
    loop :: (BS.ByteString -> IO ()) -> IO ()
    loop send = do
        msg <- mkMsg m
        send $ BSL.toStrict $ encode msg
        threadDelay sendTimeout
        loop send


main = do
    host <- getHostName
    myAddr <- hostFromSockAddr <$> addrAddress <$> head <$> getAddrInfo
        (Just (defaultHints {addrFlags = [AI_PASSIVE]}))
        (Just host) (Just $ show port)
    print $ myAddr
    state <- newMVar $ emptyMessages $ myAddr
    forkIO $ infoPrinter state
    forkIO $ server state
    client state
