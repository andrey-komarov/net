package ru.ifmo.ctddev.komarov.net.lab3.state

import ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal.{PrivateKey, PublicKey}
import scala.xml.Node

abstract class Everything {
  var myPubKey: PublicKey
  var myPrivKey: PrivateKey
  var revisions: Map[PublicKey, Set[Revision]]

}
