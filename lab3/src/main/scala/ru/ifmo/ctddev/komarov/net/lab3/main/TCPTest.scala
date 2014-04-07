package ru.ifmo.ctddev.komarov.net.lab3.main

import akka.actor._
import akka.io.{Udp, IO, Tcp}
import akka.util.ByteString
import java.net.InetSocketAddress


object TCPTest {

  object Client {
    def props(remote: InetSocketAddress, replies: ActorRef) =
      Props(classOf[Client], remote, replies)
  }

  class Logger extends Actor with ActorLogging {
    def receive = {
      case m => log info m.toString
    }
  }

  class Client(remote: InetSocketAddress, listener: ActorRef) extends Actor {

    import Tcp._
    import context.system

    IO(Tcp) ! Connect(remote)

    def receive = {
      case CommandFailed(_: Connect) =>
        listener ! "connect failed"
        context stop self

      case c@Connected(remote, local) =>
        listener ! c
        val connection = sender()
        connection ! Register(self)
        context become {
          case data: ByteString =>
            connection ! Write(data)
          case CommandFailed(w: Write) =>
            // O/S buffer was full
            listener ! "write failed"
          case Received(data) =>
            listener ! data
          case "close" =>
            connection ! Close
          case _: ConnectionClosed =>
            listener ! "connection closed"
            context stop self
        }
    }
  }

  class Server extends Actor {

    import Tcp._
    import context.system

    IO(Tcp) ! Bind(self, new InetSocketAddress("localhost", 1234))
    Udp.SO.Broadcast

    def receive = {
      case b@Bound(localAddress) =>
      // do some logging or setup ...

      case CommandFailed(_: Bind) => context stop self

      case c@Connected(remote, local) =>
        val handler = context.actorOf(Props[SimplisticHandler])
        val connection = sender()
        connection ! Register(handler)
    }

  }

  class SimplisticHandler extends Actor {

    import Tcp._

    def receive = {
      case Received(data) => sender() ! Write(data)
      case PeerClosed => context stop self
    }
  }

  class Ololo extends Actor {
    override def receive = {
      case e: String =>
        println("Hahaha " + e)
    }
  }


  def main(args: Array[String]) {
    val system = ActorSystem("MySystem")
    //    val server = system.actorOf(Props[Server], name = "server")
    //    val client = system.actorOf(Props(new Client(new InetSocketAddress("localhost", 1243), server)), name = "client")
    //    val logg = system.actorOf(Props[Logger], name = "logger")
    //    val udpL = system.actorOf(Props(new Listener(logg)), name = "UDPListener")
    //    logg ! "hhiii"
    val ololo = system.actorOf(Props[Ololo])
    import scala.concurrent.duration._
    import system.dispatcher
    system.scheduler.schedule(0 seconds, 1 second) {
      ololo ! (" " + System.currentTimeMillis())
    }
    //    client ! ByteString("asfasd")
  }
}
