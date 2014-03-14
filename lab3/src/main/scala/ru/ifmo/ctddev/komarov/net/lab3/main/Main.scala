package ru.ifmo.ctddev.komarov.net.lab3.main

import ru.ifmo.ctddev.komarov.net.lab3.repl.{PutRequest, REPLRequest, REPL}
import scala.Some

object Main extends REPL[REPLRequest] {
  override val parser = x => REPLRequest.parse(x)
  override val handler = (x : Option[REPLRequest]) => x match {
    case Some(PutRequest(filename)) =>
      println("Putin " + filename)
    case None =>
      println("usage")
  }
}
