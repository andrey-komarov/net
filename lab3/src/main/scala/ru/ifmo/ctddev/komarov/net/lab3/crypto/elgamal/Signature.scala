package ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal

import ru.ifmo.ctddev.komarov.net.lab3.bytes.{BytesToBigInt, BigIntToArrayByte}

sealed case class Signature(r: BigInt, s: BigInt) {
  def getBytes = BigIntToArrayByte(128)(r) ++ BigIntToArrayByte(128)(s)
}

object Signature {
  val BYTE_LENGTH = 256

  def apply(arr: Array[Byte]): Option[Signature] = {
    if (arr.length != BYTE_LENGTH) {
      None
    } else {
      Some(Signature(
        BytesToBigInt(arr.slice(0, 128)),
        BytesToBigInt(arr.slice(128, 256))
      ))
    }
  }
}