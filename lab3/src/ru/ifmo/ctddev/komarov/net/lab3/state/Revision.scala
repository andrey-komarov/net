package ru.ifmo.ctddev.komarov.net.lab3.state

import ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal.PublicKey

case class Revision(k: PublicKey, version: Int, fileHashes: List[FileHash]) {

}
