package ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal

import java.security.{MessageDigest, SecureRandom}
import ru.ifmo.ctddev.komarov.net.lab3.crypto.SHA256

class ElGamal(params : Params, x : PrivateKey, y : PublicKey) extends Serializable {
  import ElGamal._
  import params._

  val pubKey = y
  val privKey = x

  def sign(data : Array[Byte]) : Signature = {
    while (true) {
      val k = randFromTo(1, p - 1)
      if ((k gcd (p - 1)) == BigInt(1)) {
        val r = g.modPow(k, p)
        val s = ((BigInt(SHA256(data)) - x.n * r) * k.modInverse(p - 1)) mod (p - 1)
        if (s != BigInt(0)) {
          return Signature(r, s)
        }
      }
    }
    ???
  }
}

object ElGamal {
  private val rnd = new SecureRandom

  def rand(max : BigInt) = BigInt(max.bitLength * 2, rnd) % max

  def randFromTo(left : BigInt, right : BigInt) = left + rand(right - left)

  def apply(params : Params, x : PrivateKey) = {
    val y = params.g.modPow(x.n, params.p)
    new ElGamal(params, x, PublicKey(y))
  }

  def apply(params : Params) : ElGamal = {
    val x = randFromTo(2, params.p - 1)
    this(params, PrivateKey(x))
  }
}