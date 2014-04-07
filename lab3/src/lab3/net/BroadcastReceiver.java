package lab3.net;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public abstract class BroadcastReceiver implements Runnable {
    private final int port;

    public BroadcastReceiver(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket(port);
            socket.setBroadcast(true);
            socket.setReuseAddress(true);
            byte[] buf = new byte[1 << 17];
            DatagramPacket dp = new DatagramPacket(buf, buf.length);
            while (true) {
                socket.receive(dp);
                InetAddress addr = dp.getAddress();
                receive(addr, Arrays.copyOf(dp.getData(), dp.getLength()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void receive(InetAddress addr, byte[] message);
}
