package ru.ifmo.ctddev.komarov.net.lab3.state

import ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal.{Params, ElGamal, PublicKey}
import java.io.{PrintWriter, FileReader, BufferedReader, File}
import scala.pickling._
import json._
import java.nio.file.Files
import java.nio.charset.StandardCharsets
import java.nio.ByteBuffer

case class Everything(crypto: ElGamal) {
  var revisions: Map[PublicKey, Set[Revision]] = Map.empty
//  def revisionList: RevisionList

  def store() {
    val f = new PrintWriter(Everything.FILENAME)
    f print this.pickle
    f.close()
  }
}

object Everything {
  val FILENAME = "state.json"

  def init : Everything = restore getOrElse newInstance

  def newInstance : Everything = {
    val eg = ElGamal(Params.default)
    new Everything(eg)
  }

  def restore() : Option[Everything] = {
    try {
      val f = new File(FILENAME)
      val r = new BufferedReader(new FileReader(f))
      val contents = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(Files.readAllBytes(f.toPath))).toString
      Some(contents.unpickle[Everything])
    } catch {
      case e : Exception => {
        println(e)
        None
      }
    }
  }
}
