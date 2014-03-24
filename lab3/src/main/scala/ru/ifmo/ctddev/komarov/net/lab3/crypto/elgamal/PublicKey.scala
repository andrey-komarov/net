package ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal

import ru.ifmo.ctddev.komarov.net.lab3.bytes.Byteable._
import scala.Some
import ru.ifmo.ctddev.komarov.net.lab3.bytes.{BigIntToArrayByte, BytesToBigInt, Hex}
import akka.util.ByteString

sealed case class PublicKey(n: BigInt) extends ToBytes {


  override def toString = Hex(this.getBytes)

  override def getBytes: ByteString = ByteString(BigIntToArrayByte(128)(n))
}

object PublicKey {
  val BYTE_LENGTH = 128

  implicit object publicKey2FromBytes extends FromBytes[PublicKey] {
    override def fromByteString(bs: ByteString): Option[PublicKey] =
      if (bs.length != BYTE_LENGTH)
        None
      else
        Some(PublicKey(BytesToBigInt(bs.toArray)))
  }

  def apply(arr: Array[Byte]): Option[PublicKey] = {
    if (arr.length != BYTE_LENGTH) {
      None
    } else {
      Some(PublicKey(BytesToBigInt(arr)))
    }
  }
}