package ru.ifmo.ctddev.komarov.net.lab3.bytes

object Hex {
  def apply(a : Array[Byte]) = a.map("%02X" format _).mkString
}
