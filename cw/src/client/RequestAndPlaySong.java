package client;

import crypto.KeyPair;
import data.SongEntry;
import data.Songs;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class RequestAndPlaySong implements Runnable {
    private final KeyPair credentials;
    private final SongEntry song;
    private final InetSocketAddress addr;
    private boolean killed = false;

    public void kill() {
        killed = true;
    }

    public RequestAndPlaySong(SongEntry song, InetSocketAddress addr, KeyPair credentials) {
        this.song = song;
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

            os.write(2);
            song.store(os);

            Player player = new Player(is);
            while (player.play(5) && !killed) {

            }

        } catch (IOException | JavaLayerException e) {
            e.printStackTrace();
        }

    }
}
