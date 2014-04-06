package ru.ifmo.ctddev.komarov.net.lab3.main;

import ru.ifmo.ctddev.komarov.net.lab3.bytes.Storable;
import ru.ifmo.ctddev.komarov.net.lab3.crypto.SHA256;
import ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal.*;
import ru.ifmo.ctddev.komarov.net.lab3.net.BroadcastReceiver;
import ru.ifmo.ctddev.komarov.net.lab3.net.BroadcastSender;
import ru.ifmo.ctddev.komarov.net.lab3.net.ProtocolConfig;
import ru.ifmo.ctddev.komarov.net.lab3.net.TCPHandler;
import ru.ifmo.ctddev.komarov.net.lab3.structs.BroadcastMessage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.Optional;

public class Main {
    public static void main(String[] args) throws IOException {

        final World world = new World();

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver(ProtocolConfig.UDP_PORT) {
            @Override
            public void receive(InetAddress addr, byte[] message) {
                Optional<BroadcastMessage> oMsg = BroadcastMessage.load(new ByteArrayInputStream(message));
                if (oMsg.isPresent()) {
                    world.acceptBroadcastMessage(oMsg.get(), addr);
                }
            }
        };

        BroadcastSender broadcastSender = new BroadcastSender(ProtocolConfig.UDP_PORT, ProtocolConfig.HEARTBEAT_TIMEOUT) {
            @Override
            public Storable getMessage() {
                return world.getBroadcastMessage();
            }
        };

        TCPHandler tcpHandler = new TCPHandler(ProtocolConfig.TCP_PORT, world);

        Runnable[] toRun = new Runnable[]{broadcastReceiver, broadcastSender, tcpHandler};

        for (Runnable r : toRun) {
            new Thread(r).start();
        }
    }
}
