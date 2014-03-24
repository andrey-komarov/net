package ru.ifmo.ctddev.komarov.net.lab3.bytes


object BigIntToArrayByte {
  def apply(width: Int)(n: BigInt): Array[Byte] = {
    assert(n.bitLength <= width * 8)
    val ba = n.toByteArray
    val res = Array.fill[Byte](width - ba.length)(0) ++ ba
    res.slice(Math.max(res.length - width, 0), res.length)
  }
}
