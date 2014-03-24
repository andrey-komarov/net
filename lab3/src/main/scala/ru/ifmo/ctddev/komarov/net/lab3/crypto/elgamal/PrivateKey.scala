package ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal

import ru.ifmo.ctddev.komarov.net.lab3.bytes.{Hex, BigIntToArrayByte}

sealed case class PrivateKey(n: BigInt) extends Serializable {
  def getBytes = BigIntToArrayByte(128)(n)

  override def toString = Hex(getBytes)
}
