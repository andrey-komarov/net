package ru.ifmo.ctddev.komarov.net.lab3.actors

import akka.actor.{ActorRef, Actor}
import akka.io.{Udp, IO}
import java.net.InetSocketAddress
import akka.io.Udp.SO.Broadcast
import ru.ifmo.ctddev.komarov.net.lab3.state.HeartbeatMessage
import ru.ifmo.ctddev.komarov.net.lab3.bytes.Byteable._

class HeartbeatReceiver(port: Int, back: ActorRef) extends Actor {

  import context.system

  IO(Udp) ! Udp.Bind(self, new InetSocketAddress(port), List(Broadcast(on = true)))

  def receive = {
    case Udp.Bound(local) =>
      context.become(ready(sender()))
  }

  def ready(socket: ActorRef): Receive = {
    case Udp.Received(data, remote) =>
      fromBytes[HeartbeatMessage](data).foreach(m => back !(m, remote))
    case Udp.Unbind => socket ! Udp.Unbind
    case Udp.Unbound => context.stop(self)
  }


}
