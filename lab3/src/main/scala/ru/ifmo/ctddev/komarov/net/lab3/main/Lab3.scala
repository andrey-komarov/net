package ru.ifmo.ctddev.komarov.net.lab3.main

import akka.actor.{Props, ActorSystem}
import ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal.ElGamal
import ru.ifmo.ctddev.komarov.net.lab3.actors.Main

object Lab3 extends App {
  implicit val system = ActorSystem("UDPTest")

  val eg = ElGamal(implicitly)
  val m = system.actorOf(Props(new Main(eg)))
}
