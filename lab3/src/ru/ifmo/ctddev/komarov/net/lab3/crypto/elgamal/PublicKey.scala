package ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal

import ru.ifmo.ctddev.komarov.net.lab3.bytes.{Hex, BigIntToBytes}

sealed case class PublicKey(n: BigInt) {
  def getBytes = BigIntToBytes(128)(n)

  override def toString = Hex(getBytes)
}
