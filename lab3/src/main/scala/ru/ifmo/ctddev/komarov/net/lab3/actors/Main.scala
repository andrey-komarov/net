package ru.ifmo.ctddev.komarov.net.lab3.actors

import akka.actor.{Props, Actor}
import ru.ifmo.ctddev.komarov.net.lab3.state.{RevisionList, FileInfo, RevisionFiles, HeartbeatMessage}
import ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal.{ElGamal, PublicKey}
import ru.ifmo.ctddev.komarov.net.lab3.crypto.SHA256Hash
import scala.collection.mutable
import java.net.InetSocketAddress

object HeartbeatRequest {}

class Main(crypto: ElGamal) extends Actor {

  import context._
  import scala.concurrent.duration._

  system.scheduler.schedule(0 seconds, 2 seconds) {
    self ! HeartbeatRequest
  }

  var revisions: Map[PublicKey, mutable.SortedSet[RevisionFiles]] = Map.empty

  var files: mutable.Map[SHA256Hash, FileInfo] = mutable.Map.empty

  var knownRevListHashes: Set[SHA256Hash] = Set.empty

  def revisionList: RevisionList = {
    val ss = for (
      (k, s) <- revisions
    ) yield (k, s.map(_.version).max)
    RevisionList(ss.toList)
  }

  def nextBroadcastMessage = HeartbeatMessage(crypto.pubKey, revisionList.hash).getBytes

  val broadcaster = actorOf(Props(new Broadcaster(3012)))
  val udpReceiver = actorOf(Props(new HeartbeatReceiver(3012, self)))

  override def receive = {
    case HeartbeatRequest =>
      broadcaster ! nextBroadcastMessage
    case (HeartbeatMessage(key, hash), a: InetSocketAddress) =>
      println("Received from " + a.getAddress)
  }
}
