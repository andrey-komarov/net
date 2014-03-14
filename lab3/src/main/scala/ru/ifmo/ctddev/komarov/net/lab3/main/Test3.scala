package ru.ifmo.ctddev.komarov.net.lab3.main

import scala.concurrent._
import ru.ifmo.ctddev.komarov.net.lab3.network.udp.{BroadcastReceiver, BroadcastForever}

object Test3 {
  def main(args: Array[String]) {
    new Thread(
      new BroadcastForever(1000, 1234, System.currentTimeMillis().toString.getBytes)
    ).start()
    new Thread(
      new BroadcastReceiver(1234, x => println(new String(x)))
    ).start()
  }
}
