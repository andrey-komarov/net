package ru.ifmo.ctddev.komarov.net.lab3.state.files

import java.io.{IOException, FileOutputStream, ByteArrayOutputStream, File}
import ru.ifmo.ctddev.komarov.net.lab3.crypto.{SHA256Hash, SHA256}
import ru.ifmo.ctddev.komarov.net.lab3.bytes.ToHex
import java.nio.file.Files

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
    try {
      val fname = dir.toPath.resolve(hash.toString)
      val bytes = Files.readAllBytes(fname)
      if (SHA256(bytes) == hash) {
        Some(bytes)
      } else {
        println("Hash mismatch. Removing file " + fname)
        None
      }
    } catch {
      case e: IOException => None
    }
  }
}

