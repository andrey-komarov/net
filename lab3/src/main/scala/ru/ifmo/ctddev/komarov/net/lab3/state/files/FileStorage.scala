package ru.ifmo.ctddev.komarov.net.lab3.state.files

import java.io.{FileOutputStream, ByteArrayOutputStream, File}
import ru.ifmo.ctddev.komarov.net.lab3.crypto.{SHA256Hash, SHA256}
import ru.ifmo.ctddev.komarov.net.lab3.bytes.ToHex

object FileStorage {
  val DIRNAME = ".files"

  lazy val dir = new File(DIRNAME)

  if (!dir.exists()) {
    println("creating directory")
    dir.mkdir()
  }

  def check() {

  }

  def store(os: ByteArrayOutputStream) = {
    val fname = SHA256(os.toByteArray).toString
    val file = dir.toPath.resolve(fname).toFile
    if (!file.exists) {
      println("Saving " + file)
      file.createNewFile()
      val fos = new FileOutputStream(file)
      os.writeTo(fos)
      fos.close()
    }
  }

  def apply(hash: SHA256Hash): Option[Array[Byte]] = {
    ???
  }
}

