package ru.ifmo.ctddev.komarov.net.lab3.main

object Obj {

  trait A {
    def ololo: Int
  }

  // Compiles
  case class B(ololo: Int) extends A {}

  // Complile error
  //  case class C(ololo2: Int) extends A {}
}
