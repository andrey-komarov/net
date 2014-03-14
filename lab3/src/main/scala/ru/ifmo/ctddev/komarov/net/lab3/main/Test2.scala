package ru.ifmo.ctddev.komarov.net.lab3.main

import ru.ifmo.ctddev.komarov.net.lab3.state.Everything
import scala.xml.XML
import scala.pickling._
import json._

object Test2 {
  case class A(a : Int, b : String) {

  }

  def main(args: Array[String]) {
    val pckl = List("1", "2", "3").pickle.value
    println(pckl)
    val lst = pckl.unpickle[List[String]]
    println(lst)

    val aaa = new A(1, "atata\"")
    val p = aaa.pickle.value
    println(p)
    println(p.unpickle[A])
  }
}
