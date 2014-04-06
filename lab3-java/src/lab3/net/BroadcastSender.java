package lab3.net;

import lab3.bytes.Storable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

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
            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);
            InetSocketAddress addr = new InetSocketAddress("255.255.255.255", port);
            while (true) {
                Storable message = getMessage();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                message.store(baos);
                byte[] bytes = baos.toByteArray();
                DatagramPacket dp = new DatagramPacket(bytes, bytes.length, addr);
                socket.send(dp);
                Thread.sleep(intervalMillis);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public abstract Storable getMessage();
}
