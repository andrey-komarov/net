package ru.ifmo.ctddev.komarov.net.lab3.main

import java.io.File
import ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal.{PrivateKey, ElGamal, Params}
import ru.ifmo.ctddev.komarov.net.lab3.bytes.{BigIntToBytes, ToHex}
import java.util
import ru.ifmo.ctddev.komarov.net.lab3.crypto.SHA256
import java.nio.channels.Channels

object FilesOlolo {
  def main(args: Array[String]) {
    val eg = ElGamal(params)
//    println(eg.pubKey.toString.length)
//    println(eg.privKey.toString.length)
//    println(Hex(Array[Byte](0,1,2,3,5,30)))
//    println(util.Arrays.toString(BigIntToBytes(10)(10000)))
//    println(params.p.bitLength)
    val ee = ElGamal(Params.default, PrivateKey(BigInt("9223372036854775808")))
    println(ee.pubKey.n.bitLength)
    println()
    println(ee.pubKey.toString.length)
//    val f = new File(ee.pubKey.toString)
//    print(f.createNewFile())
    println(SHA256(ee.pubKey.toString).length)

  }

  def params = Params(
    g = 65537,
    p = BigInt("a56196d86790c6c1c12ce336a362815ca663d1661839145d3ac4cbd3ad9c810a245ef972b9fb88210b8a5c592f9dad013f185091064b7c27fc1e2e075b3392c013e5a4601e9fe6c64d34f2a33f6eb5c8bd760b35be1cf2fed97433c81239f3021de62dfdb1fb91ed785cc8dacfe816b2dcf2505f87608577a5a191f3078f0c3d", 16)
  )
}
