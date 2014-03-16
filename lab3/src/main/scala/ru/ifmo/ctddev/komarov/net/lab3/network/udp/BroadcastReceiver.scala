package ru.ifmo.ctddev.komarov.net.lab3.network.udp

import java.nio.channels.DatagramChannel
import java.net.InetSocketAddress
import java.nio.ByteBuffer

class BroadcastReceiver(port: Int, cont: Array[Byte] => Unit) extends Runnable {
  def run(): Unit = {
    val chan = DatagramChannel.open()
    chan.socket.setBroadcast(true)
    chan.socket.setReuseAddress(true)
    chan.bind(new InetSocketAddress(port))
    val bb = ByteBuffer.allocate(1 << 16)
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
