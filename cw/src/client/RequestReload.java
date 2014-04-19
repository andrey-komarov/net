package client;

import crypto.KeyPair;
import data.Songs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class RequestReload implements Runnable {
    private final InetSocketAddress addr;
    private final KeyPair credentials;

    public RequestReload(InetSocketAddress addr, KeyPair credentials) {
        this.addr = addr;
        this.credentials = credentials;
    }

    @Override
    public void run() {
        Socket socket = new Socket();
        try {
            socket.connect(addr);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            Authentificate.run(credentials, is, os);

            os.write(3);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
