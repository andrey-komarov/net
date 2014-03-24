package ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal

import ru.ifmo.ctddev.komarov.net.lab3.bytes.{Hex, BigIntToArrayByte}
import ru.ifmo.ctddev.komarov.net.lab3.bytes.Byteable.ToBytes
import akka.util.ByteString

sealed case class PrivateKey(n: BigInt) extends ToBytes {
  def getBytes = ByteString(BigIntToArrayByte(128)(n))

  override def toString = Hex(getBytes)
}
