package client;

import crypto.KeyPair;
import data.Songs;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class World {
    private final KeyPair keys;
    private final InetSocketAddress askFrom;
    private Songs songs;
    private RequestAndPlaySong playsNow;

    public World(InetAddress server, KeyPair keys) {
        askFrom = new InetSocketAddress(server, 3333);
        this.keys = keys;
        songs = null;
    }

    public synchronized void receiveSongsList(Songs newSongs) {
        System.out.println("New songs list received: ");
        songs = newSongs;
        newSongs.prettyPrint(System.out);
    }

    public synchronized void updateSongsList() {
        new Thread(new RequestSongsList(this, askFrom, keys)).start();
    }

    public synchronized void sendReload() {
        new Thread(new RequestReload(askFrom, keys)).start();
    }

    public synchronized void playSong(int n) {
        if (!(0 <= n && n < songs.songs.size())) {
            System.out.println("Incorrect index");
        } else {
            if (playsNow != null) {
                System.err.println("Interrupting " + playsNow);
                playsNow.kill();
            }
            playsNow = new RequestAndPlaySong(songs.get(n).get(), askFrom, keys);
            new Thread(playsNow).start();
        }
    }
}
