package ru.ifmo.ctddev.komarov.net.lab3.main

import ru.ifmo.ctddev.komarov.net.lab3.state.Everything

import scala.pickling._
import json._
import ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal.PublicKey


object Test4 {
  def main(args: Array[String]) {
    val e = Everything.init
    e.revisions = e.revisions.+((e.crypto.pubKey, Set.empty))
    val s = e.pickle
    println(s)
//    val e2 = s.unpickle[Everything]
//    println(e2.crypto)
  }
}
