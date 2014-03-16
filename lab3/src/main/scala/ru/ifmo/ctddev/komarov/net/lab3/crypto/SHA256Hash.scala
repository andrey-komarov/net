package ru.ifmo.ctddev.komarov.net.lab3.crypto

import ru.ifmo.ctddev.komarov.net.lab3.bytes.ToHex
import java.util.Arrays

case class SHA256Hash(bytes: Array[Byte]) extends Serializable {
  assert(bytes.length == SHA256Hash.byteLength, "Found: " + bytes.length)

  override def equals(obj: scala.Any): Boolean = obj match {
    case SHA256Hash(b) => Arrays.equals(b, bytes)
    case _ => false
  }

  override def toString = ToHex(bytes)
}

case object SHA256Hash {
  val byteLength = 32
}