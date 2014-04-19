package server;

import bytes.ByteArrayStorer;
import crypto.*;
import data.Songs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;
import java.util.Random;

public class Server {
    public static Random rnd = new Random(3);

    public static boolean authentificate(Socket s) throws IOException {
        System.err.println("Authentificating " + s);
        byte[] randBytes = new byte[128];
        rnd.nextBytes(randBytes);

        OutputStream os = s.getOutputStream();
        new ByteArrayStorer(randBytes).store(os);

        InputStream is = s.getInputStream();
        PublicKey key = PublicKey.loadFrom(is).get();
        Signature sig = Signature.loadFrom(is).get();
        Verifier v = new Verifier(Params.DEFAULT);

        if (v.verify(sig, key, randBytes)) {
            System.err.println(key.toShortString() + " authorised successfully");
            return true;
        } else {
            return false;
        }
    }

    public static boolean process(Socket s, Songs songs) throws IOException {
        InputStream is = s.getInputStream();
        int r = is.read();
        switch (r) {
            case 1:
                return songs.store(s.getOutputStream());
            case 2:
                SHA256Hash hash = SHA256Hash.loadFrom(is).get();
                return songs.store(hash, s.getOutputStream());
            case 3:
                songs.reload();
            default:
                return false;
        }
    }

    public static void main(String[] args) throws IOException {
        final Songs archive = new Songs(new File("."));

        ServerSocket server = new ServerSocket(3333);
        while (true) {
            final Socket sock = server.accept();
            System.err.println("Accepted " + sock);
            new Thread(() -> {
                try {
                    boolean res = authentificate(sock) && process(sock, archive);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        sock.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
