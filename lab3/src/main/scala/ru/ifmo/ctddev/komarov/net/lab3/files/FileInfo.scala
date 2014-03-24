//package ru.ifmo.ctddev.komarov.net.lab3.files
//
//import ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal.PublicKey
//import java.io.ByteArrayOutputStream
//import ru.ifmo.ctddev.komarov.net.lab3.bytes.Bytes
//import java.nio.ByteBuffer
//
//sealed case class FileInfo(key: PublicKey, name: String, content: Array[Byte]) extends Serializable {
//  def getBytes: Array[Byte] = {
//    val baos = new ByteArrayOutputStream
//    baos write key.getBytes
//    val nameBytes = name getBytes "UTF-8"
//    baos write Bytes(nameBytes.length)
//    baos write Bytes(content.length)
//    baos write nameBytes
//    baos write content
//    baos.toByteArray
//  }
//}
//
//object FileInfo {
//  def apply(bytes: Array[Byte]): Option[FileInfo] = {
//    if (bytes.length < PublicKey.BYTE_LENGTH + 4 + 4)
//      return None
//    val bb = ByteBuffer.wrap(bytes)
//    val key = PublicKey(Array.fill[Byte](PublicKey.BYTE_LENGTH)(bb.get))
//    val nameLength = bb.getInt
//    val contentLength = bb.getInt
//    if (bb.remaining < nameLength)
//      return None
//    val name = new String(Array.fill[Byte](nameLength)(bb.get), "UTF-8")
//    if (bb.remaining != contentLength)
//      return None
//    val content = Array.fill[Byte](contentLength)(bb.get)
//    for (k <- key) yield FileInfo(k, name, content)
//  }
//}
