package ru.ifmo.ctddev.komarov.net.lab3.bytes

import java.nio.ByteBuffer

object Bytes {
  def apply(n: Int) = ByteBuffer.allocate(4).putInt(n).array
}
