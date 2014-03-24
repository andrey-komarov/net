//package ru.ifmo.ctddev.komarov.net.lab3.network.udp
//
//import ru.ifmo.ctddev.komarov.net.lab3.state.{HeartbeatMessage, Everything}
//
//class BroadcastReceiver(world: Everything) extends AbstractBroadcastReceiver(3012) {
//  override def cont(data: Array[Byte]): Unit = {
//    HeartbeatMessage(data) match {
//      case Some(msg) =>
//        println("Received " + msg)
//        world.update(msg)
//      case None =>
//        println("Dropped bad broadcast message")
//    }
//  }
//}
