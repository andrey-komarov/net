package lab3.structs;

import lab3.bytes.Storable;
import lab3.crypto.SHA256Hash;
import lab3.crypto.elgamal.PublicKey;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;

public class BroadcastMessage implements Storable {
    public final PublicKey key;
    public final SHA256Hash hash;

    public BroadcastMessage(PublicKey key, SHA256Hash hash) {
        this.key = key;
        this.hash = hash;
    }

    public static Optional<BroadcastMessage> load(InputStream is) {
        return PublicKey.loadFrom(is).flatMap(kk ->
                        SHA256Hash.loadFrom(is).map(hh -> new BroadcastMessage(kk, hh))
        );
    }

    @Override
    public boolean store(OutputStream os) {
        return key.store(os) && hash.store(os);
    }

    public static int byteLength() {
        return PublicKey.byteLength() + SHA256Hash.byteLength();
    }
}
