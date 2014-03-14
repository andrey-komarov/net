package ru.ifmo.ctddev.komarov.net.lab3.main

import ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal.{ElGamal, Params, Verifier}
import ru.ifmo.ctddev.komarov.net.lab3.network.udp.BroadcastForever

import scala.concurrent._

class Sender extends Runnable {
  override def run(): Unit = {
    import java.net.{Inet4Address, InetSocketAddress, ProtocolFamily}
    import java.nio.ByteBuffer
    import java.nio.channels.DatagramChannel
    val chan = DatagramChannel.open()
    chan.socket().setBroadcast(true)
    while (true) {
      val bb = ByteBuffer.wrap("Hello!".getBytes)
      chan.send(bb, new InetSocketAddress("255.255.255.255", 1234))
      Thread.sleep(1000)
      print("!!")
    }
  }
}

object Main2 {
  def main(args : Array[String]) {
//    new BroadcastForever(???).run()
//    spawn {
//      print(123)
//    }
  }

  def params = Params(
    g = 65537,
    p = BigInt("a56196d86790c6c1c12ce336a362815ca663d1661839145d3ac4cbd3ad9c810a245ef972b9fb88210b8a5c592f9dad013f185091064b7c27fc1e2e075b3392c013e5a4601e9fe6c64d34f2a33f6eb5c8bd760b35be1cf2fed97433c81239f3021de62dfdb1fb91ed785cc8dacfe816b2dcf2505f87608577a5a191f3078f0c3d", 16)
  )
}
