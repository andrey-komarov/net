package ru.ifmo.ctddev.komarov.net.lab3.repl

sealed trait REPLRequest

case class PutRequest(filename: String) extends REPLRequest

object REPLRequest {
  def parse(ss: String) : Option[REPLRequest] = {
    val s = ss.trim
    if (s startsWith "put") {
      Some(PutRequest(s.substring(3).trim))
    } else {
      None
    }
  }
}
