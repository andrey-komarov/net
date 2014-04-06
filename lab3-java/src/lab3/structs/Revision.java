package lab3.structs;

import lab3.bytes.IntLoader;
import lab3.bytes.Storable;
import lab3.crypto.elgamal.PublicKey;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;

public class Revision implements Storable {
    public final PublicKey key;
    public final int version;

    public Revision(PublicKey key, int version) {
        this.key = key;
        this.version = version;
    }

    @Override
    public boolean store(OutputStream os) {
        return key.store(os) && Storable.storeInt(version, os);
    }

    public static int byteLength() {
        return PublicKey.byteLength() + 4;
    }

    public static Optional<Revision> loadFrom(InputStream is) {
        return PublicKey.loadFrom(is).flatMap(k -> IntLoader.loadFrom(is).map(v -> new Revision(k, v)));
    }

    @Override
    public String toString() {
        return "Revision{" +
                "key=" + key +
                ", version=" + version +
                '}';
    }
}
