package ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal

import ru.ifmo.ctddev.komarov.net.lab3.bytes.{BytesToBigInt, ToHex, BigIntToBytes}

sealed case class PublicKey(n: BigInt) extends Serializable {
  def getBytes = BigIntToBytes(128)(n)

  override def toString = ToHex(getBytes)
}

object PublicKey {
  val BYTE_LENGTH = 128

  def apply(arr: Array[Byte]) : Option[PublicKey] = {
    if (arr.length != BYTE_LENGTH) {
      None
    } else {
      Some(PublicKey(BytesToBigInt(arr)))
    }
  }
}