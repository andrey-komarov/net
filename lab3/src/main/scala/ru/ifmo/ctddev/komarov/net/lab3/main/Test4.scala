package ru.ifmo.ctddev.komarov.net.lab3.main

import ru.ifmo.ctddev.komarov.net.lab3.state.{Revision, BroadcastMessage, Everything}

import ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal.PublicKey
import ru.ifmo.ctddev.komarov.net.lab3.network.udp.{BroadcastReceiver, BroadcastForever}
import ru.ifmo.ctddev.komarov.net.lab3.files.FileStorage
import java.io.ByteArrayOutputStream
import ru.ifmo.ctddev.komarov.net.lab3.crypto.SHA256

object Test4 {
  def main(args: Array[String]) {
    val e = Everything.init
    e.revisions = e.revisions.+((e.crypto.pubKey, Set(Revision(e.crypto.pubKey, 0, List.empty))))

    val baos = new ByteArrayOutputStream()
    baos.write(1)
    FileStorage.store(baos)
//    new Thread(new BroadcastForever(1000, 1234, e.nextBroadcastMessage.getBytes)).start()
//
//    new Thread(
//      new BroadcastReceiver(1234, x => BroadcastMessage.fromBytes(x))
//    ).start()

    println(e.nextBroadcastMessage)
    println(FileStorage(SHA256(Array.empty)))
    println(FileStorage(SHA256(Array.fill[Byte](10)(10))))
  }
}