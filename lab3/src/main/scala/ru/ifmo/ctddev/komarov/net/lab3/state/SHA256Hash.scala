package ru.ifmo.ctddev.komarov.net.lab3.state

import ru.ifmo.ctddev.komarov.net.lab3.bytes.ToHex

case class SHA256Hash(hash: Array[Byte]) {
  assert(hash.length == SHA256Hash.byteLength, "Found: " + hash.length)

  override def toString = ToHex(hash)
}

case object SHA256Hash {
  val byteLength = 32
}