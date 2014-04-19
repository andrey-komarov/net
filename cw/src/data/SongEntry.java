package data;

import bytes.ByteArrayLoader;
import bytes.ByteArrayStorer;
import bytes.IntLoader;
import bytes.Storable;
import crypto.SHA256;
import crypto.SHA256Hash;

import java.io.*;
import java.util.Optional;

public class SongEntry implements Storable {
    private final String filename;
    private final SHA256Hash fileHash;
    private final File file;

    SongEntry(File file) throws IOException {
        this(file.getName(), SHA256.hash(new FileInputStream(file)), file);
    }

    public SHA256Hash getFileHash() {
        return fileHash;
    }

    public String getFilename() {
        return filename;
    }

    public SongEntry(String filename, SHA256Hash fileHash, File file) {
        this.filename = filename;
        this.fileHash = fileHash;
        this.file = file;
    }

    @Override
    public boolean store(OutputStream os) {
        byte[] name = filename.getBytes();
        return fileHash.store(os) && Storable.storeInt(name.length, os) && new ByteArrayStorer(name).store(os);
    }

    public boolean storeContent(OutputStream os) {
        if (!Storable.storeInt((int) file.length(), os)) {
            return false;
        }

        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] buf = new byte[1 << 16];
            int r;
            while ((r = fis.read(buf)) != -1) {
                os.write(buf, 0, r);
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static Optional<SongEntry> loadFrom(InputStream is) {
        Optional<SHA256Hash> oHash = SHA256Hash.loadFrom(is);
        if (!oHash.isPresent()) {
            return Optional.empty();
        }
        Optional<Integer> oLen = IntLoader.loadFrom(is);
        if (!oLen.isPresent()) {
            return Optional.empty();
        }
        Optional<byte[]> name = new ByteArrayLoader(oLen.get()).load(is);
        return Optional.of(new SongEntry(new String(name.get()), oHash.get(), null));
    }

    @Override
    public String toString() {
        return "SongEntry{" +
                "filename='" + filename + '\'' +
                ", fileHash=" + fileHash +
                ", file=" + file +
                '}';
    }
}
