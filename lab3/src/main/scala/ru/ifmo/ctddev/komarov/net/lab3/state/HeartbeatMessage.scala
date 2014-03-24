package ru.ifmo.ctddev.komarov.net.lab3.state

import ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal.PublicKey
import ru.ifmo.ctddev.komarov.net.lab3.crypto.SHA256Hash
import ru.ifmo.ctddev.komarov.net.lab3.bytes.Byteable._
import akka.util.ByteString

case class HeartbeatMessage(x: PublicKey, revList: SHA256Hash) extends ToBytes {
  def getBytes = x.getBytes ++ revList.getBytes
}

object HeartbeatMessage {

  implicit object heartbeatMessage2FromBytes extends FromBytes[HeartbeatMessage] {
    override def fromByteString(bs: ByteString): Option[HeartbeatMessage] =
      for {
      // :(
        true <- Some(if (bs.length == PublicKey.BYTE_LENGTH) +SHA256Hash.BYTE_LENGTH)
        k <- fromBytes[PublicKey](bs.take(PublicKey.BYTE_LENGTH))
        h <- fromBytes[SHA256Hash](bs.takeRight(SHA256Hash.BYTE_LENGTH))
      } yield HeartbeatMessage(k, h)
  }
}