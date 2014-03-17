package ru.ifmo.ctddev.komarov.net.lab3.network.udp

import java.nio.channels.DatagramChannel
import java.net.InetSocketAddress
import java.nio.ByteBuffer

abstract class AbstractBroadcastReceiver(port: Int) extends Runnable {
  def cont(data: Array[Byte]): Unit

  def run(): Unit = {
    val chan = DatagramChannel.open()
    chan.socket.setBroadcast(true)
    chan.socket.setReuseAddress(true)
    chan.bind(new InetSocketAddress(port))
    val bb = ByteBuffer.allocate(1 << 17)
    while (true) {
      val addr = chan.receive(bb)
      bb.flip()
      val ba = Array.fill[Byte](bb.remaining())(0)
      bb get ba
      bb.clear()
      cont(ba)
    }
  }
}
