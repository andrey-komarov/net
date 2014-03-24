package ru.ifmo.ctddev.komarov.net.lab3.main

import java.nio.ByteBuffer
import java.net._
import java.nio.channels.DatagramChannel

object Lab1OnlyRecv {

  def to(len: Int)(b: Array[Byte]): Array[Byte] = b ++ Array.fill[Byte](len - b.length)(0)

  def hostname() = to(20)("melchior".getBytes)

  def addr(): Array[Byte] = InetAddress.getLocalHost.getAddress

  def time(): Long = System.currentTimeMillis / 1000

  def name() = to(20)("andrey".getBytes)

  def main(args: Array[String]) {
    val chan = DatagramChannel.open()
    chan.socket().setBroadcast(true)
    chan.bind(new InetSocketAddress("localhost", 1234))
    while (true) {
      val bb = ByteBuffer.allocate(100)
      chan.receive(bb)
      println("Received" + bb.remaining())
      bb.clear()
    }
  }
}
