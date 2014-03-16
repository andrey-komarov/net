package ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal

import ru.ifmo.ctddev.komarov.net.lab3.bytes.{ToHex, BigIntToBytes}

sealed case class PublicKey(n: BigInt) extends Serializable {
  def getBytes = BigIntToBytes(128)(n)

  override def toString = ToHex(getBytes)
}

object PublicKey {
  val byteLength = 128

  def fromBytes(arr: Array[Byte]) : Option[PublicKey] = {
    if (arr.length != byteLength) {
      None
    } else {
      Some(PublicKey(BigInt.apply(Array.fill[Byte](1)(0) ++ arr)))
    }
  }
}