package ru.ifmo.ctddev.komarov.net.lab3.state

import ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal._
import ru.ifmo.ctddev.komarov.net.lab3.crypto.SHA256Hash
import java.io._
import scala.Serializable
import scala.Some

case class Everything(crypto: ElGamal) extends Serializable {
  var revisions: Map[PublicKey, Set[RevisionFiles]] = Map.empty

  var files: Map[SHA256Hash, FileInfo] = Map.empty

  def revisionList: RevisionList = {
    val ss = for (
      (k, s) <- revisions
    ) yield (k, s.map(_.version).max)
    RevisionList(ss.toList)
  }

  def nextBroadcastMessage = BroadcastMessage(crypto.pubKey, revisionList.hash)

  def store() : Boolean = {
    try {
      val fos = new FileOutputStream(Everything.FILENAME)
      val baos = new ByteArrayOutputStream()
      val oos = new ObjectOutputStream(baos)
      oos.writeObject(this)
      baos.writeTo(fos)
      fos.close()
      true
    } catch {
      case e : IOException => false
    }
  }
}

object Everything {
  val FILENAME = "state.obj"

  def init : Everything = restore getOrElse newInstance

  def newInstance : Everything = {
    val eg = ElGamal(Params.default)
    new Everything(eg)
  }

  def restore() : Option[Everything] = {
    try {
      val ois = new ObjectInputStream(new FileInputStream(FILENAME))
      val obj = ois.readObject()
      ois.close()
      Some(obj.asInstanceOf[Everything])
    } catch {
      case e : Exception =>
        println(e)
        None
    }
  }
}
