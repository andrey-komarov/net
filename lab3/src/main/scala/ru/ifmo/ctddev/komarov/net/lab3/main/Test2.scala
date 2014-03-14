package ru.ifmo.ctddev.komarov.net.lab3.main

import ru.ifmo.ctddev.komarov.net.lab3.state.Everything
import scala.xml.XML
import scala.pickling._
import json._

object Test2 {

  def main(args: Array[String]) {
//    val e = new Everything
//    val s = e.toXML.toString()
//    val xx = XML.loadString(s)
//    print(xx)
    val pckl = List("1", "2", "3").pickle.value
    println(pckl)
    val lst = pckl.unpickle[List[Int]]
    println(lst)
  }
}
