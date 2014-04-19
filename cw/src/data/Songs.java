package data;

import bytes.IntLoader;
import bytes.Storable;
import crypto.SHA256Hash;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Songs implements Storable {
    public File origin = null;
    public List<SongEntry> songs;

    public Songs(File dir) throws IOException {
        origin = dir;
        reload();
    }

    public void reload() throws IOException {
        File[] s = origin.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith("mp3");
            }
        });

        songs = new ArrayList<>();
        for (File song : s) {
            songs.add(new SongEntry(song));
        }
    }

    public Songs(List<SongEntry> songs) {
        this.songs = songs;
    }

    @Override
    public boolean store(OutputStream os) {
        if (!Storable.storeInt(songs.size(), os)) {
            return false;
        }
        for (SongEntry s : songs) {
            if (!s.store(os)) {
                return false;
            }
        }
        return false;
    }

    public Optional<SongEntry> get(SHA256Hash hash) {
        return songs.stream().filter(s -> s.getFileHash().equals(hash)).findAny();
    }

    public Optional<SongEntry> get(int pos) {
        if (0 <= pos && pos < songs.size()) {
            return Optional.of(songs.get(pos));
        } else {
            return Optional.empty();
        }
    }

    public boolean store(SHA256Hash songHash, OutputStream os) throws IOException {
        Optional<SongEntry> oSe = get(songHash);
        if (!oSe.isPresent()) {
            os.write(0);
            return true;
        } else {
            os.write(1);
            return oSe.get().storeContent(os);
        }
    }

    public static Optional<Songs> loadFrom(InputStream is) {
        Optional<Integer> oLen = IntLoader.loadFrom(is);
        if (!oLen.isPresent()) {
            return Optional.empty();
        }
        int len = oLen.get();
        ArrayList<SongEntry> songs = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            Optional<SongEntry> oSe = SongEntry.loadFrom(is);
            if (!oSe.isPresent()) {
                return Optional.empty();
            }
            songs.add(oSe.get());
        }
        return Optional.of(new Songs(songs));
    }

    @Override
    public String toString() {
        return "Songs{" +
                "songs=" + songs +
                '}';
    }

    public void prettyPrint(PrintStream out) {
        for (int i = 0; i < songs.size(); i++) {
            out.println("[" + i + "]: " + songs.get(i).getFilename());
        }
    }
}
