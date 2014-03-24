package ru.ifmo.ctddev.komarov.net.lab3.actors

import akka.actor.{Props, Actor}
import ru.ifmo.ctddev.komarov.net.lab3.state.{RevisionList, FileInfo, RevisionFiles, HeartbeatMessage}
import ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal.{ElGamal, PublicKey}
import ru.ifmo.ctddev.komarov.net.lab3.crypto.SHA256Hash

object Heartbeat {}

class Main(crypto: ElGamal) extends Actor {

  import context._
  import scala.concurrent.duration._

  system.scheduler.schedule(0 seconds, 1 second) {
    self ! Heartbeat
  }

  var revisions: Map[PublicKey, Set[RevisionFiles]] = Map.empty

  var files: Map[SHA256Hash, FileInfo] = Map.empty

  def revisionList: RevisionList = {
    val ss = for (
      (k, s) <- revisions
    ) yield (k, s.map(_.version).max)
    RevisionList(ss.toList)
  }

  def nextBroadcastMessage = HeartbeatMessage(crypto.pubKey, revisionList.hash)

  val broadcaster = actorOf(Props(new Broadcaster(3012)))
  val udpReceiver = actorOf(Props(new HeartbeatReceiver(3012, self)))

  override def receive = {
    case Heartbeat => broadcaster ! nextBroadcastMessage
    case m@HeartbeatMessage(key, hash) =>
      println("Received " + m)
      ???
  }
}
