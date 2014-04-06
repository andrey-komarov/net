package ru.ifmo.ctddev.komarov.net.lab3.net;

import ru.ifmo.ctddev.komarov.net.lab3.bytes.Storable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public abstract class BroadcastSender implements Runnable {
    private final int port;
    private final long intervalMillis;

    public BroadcastSender(int port, long intervalMillis) {
        this.port = port;
        this.intervalMillis = intervalMillis;
    }

    @Override
    public void run() {
        try {
            DatagramChannel chan = DatagramChannel.open();
            chan.socket().setBroadcast(true);
            InetSocketAddress addr = new InetSocketAddress("255.255.255.255", port);
            while (true) {
                Storable message = getMessage();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                message.store(baos);
                ByteBuffer bb = ByteBuffer.wrap(baos.toByteArray());
                chan.send(bb, addr);
                Thread.sleep(intervalMillis);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public abstract Storable getMessage();
}
