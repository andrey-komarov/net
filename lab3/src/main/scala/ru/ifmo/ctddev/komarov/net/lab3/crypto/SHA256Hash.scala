package ru.ifmo.ctddev.komarov.net.lab3.crypto

import ru.ifmo.ctddev.komarov.net.lab3.bytes.Hex
import java.util

case class SHA256Hash(bytes: Array[Byte]) extends Serializable {
  assert(bytes.length == SHA256Hash.BYTE_LENGTH, "Found: " + bytes.length)

  override def equals(obj: scala.Any): Boolean = obj match {
    case SHA256Hash(b) => util.Arrays.equals(b, bytes)
    case _ => false
  }

  override def toString = Hex(bytes)
}

case object SHA256Hash {
  val BYTE_LENGTH = 32
}