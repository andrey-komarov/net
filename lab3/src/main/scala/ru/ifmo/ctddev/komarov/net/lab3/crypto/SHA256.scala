package ru.ifmo.ctddev.komarov.net.lab3.crypto

import java.security.MessageDigest
import ru.ifmo.ctddev.komarov.net.lab3.bytes.ToHex
import ru.ifmo.ctddev.komarov.net.lab3.crypto.SHA256Hash

object SHA256 {
  private val sha256 = MessageDigest getInstance "SHA-256"

  def apply(data : Array[Byte]) = SHA256Hash(sha256 digest data)
}
