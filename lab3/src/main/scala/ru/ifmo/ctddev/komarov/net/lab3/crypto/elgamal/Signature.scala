package ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal

import ru.ifmo.ctddev.komarov.net.lab3.bytes.BigIntToBytes

sealed case class Signature (r : BigInt, s : BigInt) {
  def getBytes = BigIntToBytes(128)(r) ++ BigIntToBytes(128)(s)
}
