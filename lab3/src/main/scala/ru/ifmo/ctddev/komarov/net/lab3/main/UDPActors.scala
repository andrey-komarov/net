package ru.ifmo.ctddev.komarov.net.lab3.main

import akka.actor._
import akka.io.{Udp, IO}
import akka.util.ByteString
import java.net.InetSocketAddress


object UDPActors {

  class Sender() extends Actor {

    import context.system

    IO(Udp) ! Udp.SimpleSender

    def receive = {
      case Udp.SimpleSenderReady =>
        context.become(ready(sender))
      //      case e => println(e)
    }

    def ready(socket: ActorRef): Receive = {
      case "go" =>
        socket ! Udp.Send(ByteString("12341234"), new InetSocketAddress("127.0.0.1", 1234))
      case Udp.Unbind => socket ! Udp.Unbind
      case Udp.Unbound => context.stop(self)
    }
  }

  class Listener() extends Actor {

    import context.system

    IO(Udp) ! Udp.Bind(self, new InetSocketAddress("localhost", 1234))

    def receive = {
      case Udp.Bound(local) =>
        context.become(ready(sender()))
      case e => println(e + " " + "!!!!!")
    }

    def ready(socket: ActorRef): Receive = {
      case Udp.Received(data, remote) =>
        println(data + " " + "!!!!")
      //        val processed = // parse data etc., e.g. using PipelineStage
      //          socket ! Udp.Send(data, remote) // example server echoes back
      //        nextActor ! processed
      case Udp.Unbind => socket ! Udp.Unbind
      case Udp.Unbound => context.stop(self)
    }
  }

  class Logger extends Actor with ActorLogging {
    def receive = {
      case m => log info m.toString
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
    //    val ololo = system.actorOf(Props[Ololo])
    val sender = system.actorOf(Props[Sender])
    val recv = system.actorOf(Props[Listener])
    import scala.concurrent.duration._
    import system.dispatcher
    //    system.scheduler.schedule(0 seconds, 1 second) {
    //      ololo ! (" " + System.currentTimeMillis())
    //    }
    system.scheduler.schedule(0 seconds, 1 second) {
      sender ! "go"
    }
    //    client ! ByteString("asfasd")
  }
}
