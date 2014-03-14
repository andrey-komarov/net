package ru.ifmo.ctddev.komarov.net.lab3.state

import ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal.PublicKey
import ru.ifmo.ctddev.komarov.net.lab3.state.FileHash
import ru.ifmo.ctddev.komarov.net.lab3.bytes.ToHex
import scala.xml.Node

case class Revision(k: PublicKey, version: Int, fileHashes: List[FileHash]) {
  def toXML =
  <revision>
    <key>{k}</key>
    <version>{version}</version>
    <filehashes>
      {for (h <- fileHashes) yield <hash>{ToHex(h)}</hash>}
    </filehashes>
  </revision>
}

object Revision {
  def fromXML(node: Node) = Revision(
    k = PublicKey(BigInt((node \ "key").text)),
    version = (node \ "version").text.toInt,
    fileHashes = ???
  )
}
