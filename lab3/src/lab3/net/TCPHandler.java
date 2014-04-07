package lab3.net;

import lab3.main.World;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPHandler implements Runnable {
    private final int port;
    private final World world;

    public TCPHandler(int port, World world) {
        this.port = port;
        this.world = world;
    }

    @Override
    public void run() {
        try {
            ServerSocket server = new ServerSocket(port);
            while (true) {
                Socket client = server.accept();
                new Thread(new Router(client, world)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
