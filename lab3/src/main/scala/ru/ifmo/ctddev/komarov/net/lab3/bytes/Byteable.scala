package ru.ifmo.ctddev.komarov.net.lab3.bytes

import akka.util.ByteString
import java.nio.ByteBuffer

object Byteable {

  trait ToBytes {
    def getBytes: ByteString
  }

  implicit def int2ToBytes(n: Int) = new ToBytes {
    override def getBytes = ByteString(ByteBuffer.allocate(4).putInt(n).array)
  }

  implicit def pair2ToBytes[A <% ToBytes, B <% ToBytes](ab: (A, B)) = new ToBytes {
    override def getBytes = ab._1.getBytes ++ ab._1.getBytes
  }

  trait FromBytes[T] {
    def fromByteString(bs: ByteString): Option[T]
  }

  def fromBytes[T](bs: ByteString)(implicit fb: FromBytes[T]): Option[T] =
    fb.fromByteString(bs)

  implicit object intFromBytes extends FromBytes[Int] {
    override def fromByteString(bs: ByteString): Option[Int] =
      if (bs.length != 4)
        None
      else
        Some(ByteBuffer.wrap(bs.toArray).getInt)
  }

}