package ru.ifmo.ctddev.komarov.net.lab3.crypto

import ru.ifmo.ctddev.komarov.net.lab3.bytes.Hex
import ru.ifmo.ctddev.komarov.net.lab3.bytes.Byteable.ToBytes
import akka.util.ByteString

case class SHA256Hash(getBytes: ByteString) extends ToBytes with Serializable {
  assert(getBytes.length == SHA256Hash.BYTE_LENGTH, "Found: " + getBytes.length)

  override def toString = Hex(getBytes)

  //  override def getBytes
}

case object SHA256Hash {
  val BYTE_LENGTH = 32
}