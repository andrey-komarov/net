package ru.ifmo.ctddev.komarov.net.lab3.actors

import akka.actor.{ActorRef, Actor}
import akka.io.{Udp, IO}
import akka.io.Udp.SO.Broadcast
import akka.util.ByteString
import java.net.InetSocketAddress


class Broadcaster(port: Int) extends Actor {

  import context.system

  IO(Udp) ! Udp.SimpleSender(List(Broadcast(on = true)))

  val address = new InetSocketAddress("255.255.255.255", port)

  def receive = {
    case Udp.SimpleSenderReady =>
      context.become(ready(sender()))
  }

  def ready(socket: ActorRef): Receive = {
    case b: ByteString =>
      socket ! Udp.Send(b, address)
  }
}
