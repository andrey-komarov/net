package ru.ifmo.ctddev.komarov.net.lab3.state

import ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal.Signature
import java.nio.ByteBuffer
import ru.ifmo.ctddev.komarov.net.lab3.crypto.SHA256Hash

case class RevisionFiles(sig: Signature, version: Int, fileHashes: List[SHA256Hash]) {
  def getBytes: Array[Byte] = {
    ???
  }
}

object RevisionFiles {
  def apply(bytes: Array[Byte]): Option[RevisionFiles] = {
    if (bytes.length < Signature.BYTE_LENGTH + 4 + 4)
      return None
    val bb = ByteBuffer.wrap(bytes)
    val osig = Signature(Array.fill[Byte](Signature.BYTE_LENGTH)(bb.get))
    val ver = bb.getInt
    val len = bb.getInt
    if (bb.remaining() != len * SHA256Hash.BYTE_LENGTH) {
      return None
    }
    val s = for (i <- 1 to len) yield {
      SHA256Hash(Array.fill[Byte](SHA256Hash.BYTE_LENGTH)(bb.get))
    }
    for (sig <- osig)
    yield RevisionFiles(sig, ver, s.toList)
  }
}