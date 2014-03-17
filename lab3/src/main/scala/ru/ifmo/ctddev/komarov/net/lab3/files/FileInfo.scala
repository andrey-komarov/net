package ru.ifmo.ctddev.komarov.net.lab3.files

import ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal.PublicKey

sealed case class FileInfo(key: PublicKey, name: String, content: Array[Byte]) extends Serializable {

}

object FileInfo {
  def apply(bytes: Array[Byte]): Option[FileInfo] = {
    ???
  }
}
