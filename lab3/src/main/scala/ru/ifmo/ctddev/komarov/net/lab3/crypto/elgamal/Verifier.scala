package ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal

import ru.ifmo.ctddev.komarov.net.lab3.crypto.SHA256

sealed case class Verifier(params : Params, key : PublicKey) {
  import params.{p,g}
  import key.{n => y}

  def apply(data : Array[Byte], sig : Signature) : Boolean = {
    import sig.{r,s}
    if (!(0 < r && r < p) || !(0 < s && s < p - 1)) {
      return false
    }
    val lhs = g modPow (BigInt(SHA256(data)), p)
    val rhs = ((y modPow (r, p)) * (r modPow (s, p))) mod p
    lhs == rhs
  }
}
