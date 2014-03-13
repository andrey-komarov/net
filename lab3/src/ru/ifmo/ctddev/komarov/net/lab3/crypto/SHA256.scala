package ru.ifmo.ctddev.komarov.net.lab3.crypto

import java.security.MessageDigest
import ru.ifmo.ctddev.komarov.net.lab3.bytes.Hex

object SHA256 {
  private val sha256 = MessageDigest getInstance "SHA-256"

  def apply(data : Array[Byte]) = sha256 digest data

  def apply(data : String) : String = Hex(this(data.getBytes))
}
