package ru.ifmo.ctddev.komarov.net.lab3.crypto

import ru.ifmo.ctddev.komarov.net.lab3.bytes.Hex
import ru.ifmo.ctddev.komarov.net.lab3.bytes.Byteable.{FromBytes, ToBytes}
import akka.util.ByteString

case class SHA256Hash(getBytes: ByteString) extends ToBytes with Serializable {
  assert(getBytes.length == SHA256Hash.BYTE_LENGTH, "Found: " + getBytes.length)

  override def toString = Hex(getBytes)

  //  override def getBytes
}

case object SHA256Hash {

  implicit object sha2562FromBytes extends FromBytes[SHA256Hash] {
    override def fromByteString(bs: ByteString): Option[SHA256Hash] =
      if (bs.length != BYTE_LENGTH)
        None
      else Some(SHA256Hash(bs))
  }

  def apply(b: Array[Byte]): SHA256Hash = SHA256Hash(ByteString(b))

  val BYTE_LENGTH = 32
}