package ru.ifmo.ctddev.komarov.net.lab3.bytes

object ToHex {
  def apply(a : Array[Byte]) = a.map("%02X" format _).mkString
}
