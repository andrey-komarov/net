package ru.ifmo.ctddev.komarov.net.lab3.network.udp

import java.nio.channels.DatagramChannel
import java.nio.ByteBuffer
import java.net.InetSocketAddress
import java.lang.Thread

class BroadcastForever(timeout: Long, port: Int, msg: => Array[Byte]) extends Runnable {
  def run(): Unit = {
    val chan = DatagramChannel.open()
    chan.socket().setBroadcast(true)
    val addr = new InetSocketAddress("255.255.255.255", 1234)
    while (true) {
      println("sent")
      chan send (ByteBuffer.wrap(msg), addr)
      Thread sleep timeout
    }
  }
}