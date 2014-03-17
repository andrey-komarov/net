package ru.ifmo.ctddev.komarov.net.lab3.state

import ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal.PublicKey
import ru.ifmo.ctddev.komarov.net.lab3.crypto.SHA256Hash

case class BroadcastMessage(x: PublicKey, revList: SHA256Hash) {
  def getBytes: Array[Byte] = x.getBytes ++ revList.bytes
}

object BroadcastMessage {
  private val revListStart = PublicKey.BYTE_LENGTH
  private val end = revListStart + SHA256Hash.BYTE_LENGTH

  def fromBytes(arr: Array[Byte]): Option[BroadcastMessage] = {
    if (arr.length != PublicKey.BYTE_LENGTH) {
      None
    } else {
      for (
        k <- PublicKey(arr.slice(0, revListStart));
        h <- Some(SHA256Hash(arr.slice(revListStart, end)))
      ) yield BroadcastMessage(k, h)
    }
  }
}