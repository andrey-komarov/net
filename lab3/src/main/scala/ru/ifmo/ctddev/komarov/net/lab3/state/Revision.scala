package ru.ifmo.ctddev.komarov.net.lab3.state

import ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal.PublicKey
import ru.ifmo.ctddev.komarov.net.lab3.state.FileHash
import ru.ifmo.ctddev.komarov.net.lab3.bytes.ToHex
import scala.xml.Node

case class Revision(k: PublicKey, version: Int, fileHashes: List[FileHash]) {
}
