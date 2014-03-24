package ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal

import ru.ifmo.ctddev.komarov.net.lab3.bytes.Byteable._
import scala.Some
import ru.ifmo.ctddev.komarov.net.lab3.bytes.{BigIntToArrayByte, BytesToBigInt, Hex}
import akka.util.ByteString

sealed case class PublicKey(n: BigInt) {

  import PublicKey._

  override def toString = Hex(this.getBytes)
}

object PublicKey {
  val BYTE_LENGTH = 128

  implicit def publicKey2ToBytes(key: PublicKey): ToBytes = new ToBytes {
    override def getBytes = ByteString(BigIntToArrayByte(128)(key.n))
  }

  def apply(arr: Array[Byte]): Option[PublicKey] = {
    if (arr.length != BYTE_LENGTH) {
      None
    } else {
      Some(PublicKey(BytesToBigInt(arr)))
    }
  }
}