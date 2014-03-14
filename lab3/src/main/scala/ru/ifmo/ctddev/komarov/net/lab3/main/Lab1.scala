package ru.ifmo.ctddev.komarov.net.lab3.main

import java.nio.{ByteOrder, ByteBuffer}
import java.net._
import java.nio.channels.DatagramChannel

object Lab1 {

  def to(len : Int)(b : Array[Byte]) : Array[Byte] = b ++ Array.fill[Byte](len - b.length)(0)

  def hostname() = to(20)("melchior".getBytes)

  def addr() : Array[Byte] = InetAddress.getLocalHost.getAddress

  def time() : Long = System.currentTimeMillis / 1000

  def name() = to(20)("andrey".getBytes)

  def main(args : Array[String]) {
    val chan = DatagramChannel.open()
    chan.socket().setBroadcast(true)
    while (true) {
      val bb = ByteBuffer.allocate(52)
      bb.order(ByteOrder.BIG_ENDIAN)
      bb.put(addr()).put(hostname()).putLong(time()).put(name())
      bb.flip()
      println(chan.send(bb, new InetSocketAddress("255.255.255.255", 1234)))
      Thread.sleep(5000)
    }
  }
}
