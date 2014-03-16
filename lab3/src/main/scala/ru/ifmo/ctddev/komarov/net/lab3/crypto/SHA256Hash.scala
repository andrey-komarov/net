package ru.ifmo.ctddev.komarov.net.lab3.crypto

import ru.ifmo.ctddev.komarov.net.lab3.bytes.ToHex

case class SHA256Hash(bytes: Array[Byte]) {
  assert(bytes.length == SHA256Hash.byteLength, "Found: " + bytes.length)

  override def toString = ToHex(bytes)
}

case object SHA256Hash {
  val byteLength = 32
}