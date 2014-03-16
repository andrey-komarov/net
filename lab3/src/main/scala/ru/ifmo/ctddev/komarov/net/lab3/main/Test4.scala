package ru.ifmo.ctddev.komarov.net.lab3.main

import ru.ifmo.ctddev.komarov.net.lab3.state.{BroadcastMessage, Everything}

import ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal.PublicKey
import ru.ifmo.ctddev.komarov.net.lab3.network.udp.{BroadcastReceiver, BroadcastForever}


object Test4 {
  def main(args: Array[String]) {
    val e = Everything.init
//    new Thread(new BroadcastForever(1000, 1234, e.nextBroadcastMessage.getBytes)).start()
//
//    new Thread(
//      new BroadcastReceiver(1234, x => BroadcastMessage.fromBytes(x))
//    ).start()

    print(e.nextBroadcastMessage)
  }
}
