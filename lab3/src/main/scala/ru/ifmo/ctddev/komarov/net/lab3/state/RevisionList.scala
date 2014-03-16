package ru.ifmo.ctddev.komarov.net.lab3.state

import ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal.PublicKey
import com.sun.xml.internal.ws.util.ByteArrayBuffer
import java.io.ByteArrayOutputStream
import ru.ifmo.ctddev.komarov.net.lab3.crypto.SHA256

case class RevisionList(revisions: List[(PublicKey, Int)]) {
  def hash() : SHA256Hash = {
    val baos = new ByteArrayOutputStream()
    revisions.sortBy(_._1.n).foreach(p => {
      baos.write(p._1.getBytes)
      baos.write(p._2)
    })
    SHA256Hash(SHA256(baos.toByteArray))
  }
}
