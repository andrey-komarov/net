package ru.ifmo.ctddev.komarov.net.lab3.state

import ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal.PublicKey

case class BroadcastMessage(x: PublicKey, revList: SHA256Hash) {
  def getBytes : Array[Byte] = x.getBytes ++ revList.hash
}

object BroadcastMessage {
  private val revListStart = PublicKey.byteLength
  private val end = revListStart + SHA256Hash.byteLength

  def fromBytes(arr: Array[Byte]) : Option[BroadcastMessage] = {
    if (arr.length != PublicKey.byteLength) {
      None
    } else {
      for (
        k <- PublicKey.fromBytes(arr.slice(0, revListStart));
        h <- Some(SHA256Hash(arr.slice(revListStart, end)))
      ) yield BroadcastMessage(k, h)
    }
  }
}