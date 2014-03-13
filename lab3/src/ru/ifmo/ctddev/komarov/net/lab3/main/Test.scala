package ru.ifmo.ctddev.komarov.net.lab3.main

import java.nio.channels.SocketChannel
import java.net.InetSocketAddress
import java.nio.ByteBuffer

object Test {
  def main(args: Array[String]) {
    val chan = SocketChannel.open(new InetSocketAddress("localhost", 6666))
    val bb = ByteBuffer.allocate(10)
    while(chan.read(bb) != -1) {
      println(bb.remaining)
      Thread.sleep(1000)
      bb.flip()
      chan.write(bb)
      bb.compact()
      BigInt
    }
  }
}
