package ru.ifmo.ctddev.komarov.net.lab3.bytes

import akka.util.ByteString

object Hex {
  def apply(a: ByteString): String = a.map("%02X" format _).mkString
}
