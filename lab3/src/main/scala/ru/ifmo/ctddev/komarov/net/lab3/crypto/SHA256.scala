package ru.ifmo.ctddev.komarov.net.lab3.crypto

import java.security.MessageDigest

object SHA256 {
  private val sha256 = MessageDigest.getInstance("SHA-256")

  def apply(data: Array[Byte]) = SHA256Hash(sha256 digest data)
}
