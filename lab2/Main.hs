import HeartBeatProtocol
import UDPReceiver
import UDPBroadcaster

import Control.Concurrent

main = do
    forkIO $ broadcastReceiver $ \(h, s) -> print (idHB h)
    broadcaster
