package org.kafecho.learning.akka

import java.net.InetSocketAddress
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.io.IO
import akka.io.Udp
import akka.io.Udp.Bind
import akka.io.Udp.Bound
import akka.io.Udp.Received
import akka.io.Udp.Unbind

object Connect

object Disconnect

object AkkaIOTest extends App {
  implicit val system = ActorSystem("UDPTest")

  class UDPConnection(port: Int) extends Actor {
    val handler = system.actorOf(Props[UDPReceiver], name = "UDPReceiver")

    def receive = {
      case Connect =>
        IO(Udp) ! Bind(handler, new InetSocketAddress(port))

      case Bound =>
        val worker = sender
        context.become {
          case Disconnect =>
            worker ! Unbind
            context.become(receive)
        }
    }
  }

  class UDPReceiver extends Actor with ActorLogging {
    def receive = {
      case Received(data, from) => log.info("Received a UDP Packet {} sent from {}.", data, from)
      case _ =>
    }
  }

  val udpConnection = system.actorOf(Props(new UDPConnection(1234)))
  udpConnection ! Connect
  Thread.sleep(2000)
  udpConnection ! Disconnect
  Thread.sleep(2000)
  udpConnection ! Connect
  Thread.sleep(2000)
  udpConnection ! Disconnect
}