package ru.ifmo.ctddev.komarov.net.lab3.actors

import akka.actor.{Props, Actor}

object Heartbeat {}

case class

class Main extends Actor {

  import context._

  val broadcaster = actorOf(Props(new Broadcaster(3012)))
  val udpReceiver = actorOf(Props(new BroadcastReceiver(3012, self)))

  override def receive = {
    case Heartbeat => broadcaster ! ???
    case
  }
}
