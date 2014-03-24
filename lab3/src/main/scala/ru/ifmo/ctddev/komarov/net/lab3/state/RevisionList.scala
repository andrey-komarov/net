package ru.ifmo.ctddev.komarov.net.lab3.state

import ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal.PublicKey
import ru.ifmo.ctddev.komarov.net.lab3.crypto.SHA256

case class RevisionList(revisions: List[(PublicKey, Int)]) {
  //  def getBytesWithoutLength: Array[Byte] = {
  //    val baos = new ByteArrayOutputStream()
  //    revisions.sortBy(_._1.n).foreach(p => {
  //      baos.write(p._1.getBytes)
  //      baos.write(ByteBuffer.allocate(4).putInt(p._2).array())
  //    })
  //    baos.toByteArray
  //  }
  //
  //  def getBytes: Array[Byte] =
  //    ByteBuffer.allocate(4).putInt(revisions.length).array() ++ getBytesWithoutLength
  //
  //  def hash: SHA256Hash = SHA256(getBytesWithoutLength)
  def hash = SHA256("asdf".getBytes)
}

//object RevisionList {
//  def apply(bytes: Array[Byte]): Option[RevisionList] = {
//    if (bytes.length < 4)
//      return None
//    val bb = ByteBuffer.wrap(bytes)
//    val len = bb.getInt
//    if (len * (PublicKey.BYTE_LENGTH + 4) + 4 != bytes.length)
//      return None
//    val sss = for (i <- 1 to len) yield {
//      val key = Array.fill[Byte](PublicKey.BYTE_LENGTH)(bb.get)
//      val n = bb.getInt
//      (PublicKey(key).get, n)
//    }
//    Some(RevisionList(sss.toList))
//  }
//}
