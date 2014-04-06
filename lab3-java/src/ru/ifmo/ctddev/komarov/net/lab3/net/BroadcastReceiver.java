package ru.ifmo.ctddev.komarov.net.lab3.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public abstract class BroadcastReceiver implements Runnable {
    private final int port;

    public BroadcastReceiver(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            DatagramChannel chan = DatagramChannel.open();
            chan.socket().setBroadcast(true);
            chan.socket().setReuseAddress(true);
            chan.bind(new InetSocketAddress(port));
            ByteBuffer bb = ByteBuffer.allocate(1 << 17);
            while (true) {
                SocketAddress addr = chan.receive(bb);
                bb.flip();
                byte[] bytes = new byte[bb.remaining()];
                bb.get(bytes);
                bb.clear();
                receive(addr, bytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void receive(SocketAddress addr, byte[] message);
}
