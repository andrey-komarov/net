{-----------------------------------------------------------------------------
    reactive-banana-wx
    
    Example: Counter
------------------------------------------------------------------------------}
{-# LANGUAGE ScopedTypeVariables #-} -- allows "forall t. Moment t"
{-# LANGUAGE RecursiveDo #-}
{-# LANGUAGE Rank2Types #-}

import UDPReceiver
import UDPBroadcaster
import HeartBeatProtocol

import Control.Concurrent

import Control.Monad

import Graphics.UI.WX hiding (Event)
import Reactive.Banana
import Reactive.Banana.WX

import qualified Data.Map as M
import Data.Word
import Network.Info

type ChatMessage = String
type Time = Word64

type Users = M.Map MAC Time

{-----------------------------------------------------------------------------
    Main
------------------------------------------------------------------------------}
main = start $ do
    f       <- frame [text := "Counter"]
    chat    <- singleListBox f []
    bup     <- button f [text := "Up"]
    bdown   <- button f [text := "Down"]
    output  <- staticText f []

    finput  <- entry f [processEnter := True]

    (recvH, recvFire) <- newAddHandler

    set f [layout := margin 10 $
            column 5 [widget bup, 
                      widget bdown, 
                      widget output, 
                      minsize (sz 300 300) (widget chat),
                      widget finput
                     ]
          ]

    let networkDescription :: forall t. Frameworks t => Moment t ()
        networkDescription = do
        
        eBroadcast <- fromAddHandler recvH
        let eMACs :: Event t String  = fmap (show . idHB . fst) eBroadcast

        eup    <- event0 bup   command
        edown  <- event0 bdown command
        (eChat :: Event t ())  <- event0 finput command
        
        emsg <- eventText finput

        reactimate $ fmap print eChat

        let
            (bmsg :: Behavior t String) = stepper "" emsg
            bAddMsg = (:) <$> bmsg

            bChatMessages :: Behavior t [ChatMessage]
            bChatMessages = accumB [] $ unions [
                    bAddMsg <@ eChat
                    , (:) <$> eMACs
             ]

            counter :: Behavior t Int
            counter = accumB 0 $ ((+1) <$ eup) `union` (subtract 1 <$ edown)
    
        sink output [text :== show <$> counter] 
        sink chat   [items :== bChatMessages]

    network <- compile networkDescription    
    actuate network
    forkIO broadcaster
    forkIO $ broadcastReceiver recvFire
