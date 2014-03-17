package ru.ifmo.ctddev.komarov.net.lab3.bytes

object BytesToBigInt {
  def apply(arr: Array[Byte]): BigInt = BigInt(Array.fill[Byte](1)(0) ++ arr)
}
