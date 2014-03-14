package ru.ifmo.ctddev.komarov.net.lab3.repl

import java.io.{BufferedReader, InputStreamReader}

trait REPL[T] {

  val parser: String => Option[T]
  val handler: Option[T] => Unit

  def main(args: Array[String]) {
    val r = new BufferedReader(new InputStreamReader(System.`in`))
    while (true) {
      print(">>> ")
      handler(parser(r.readLine()))
    }
  }
}
