package lab3.structs;

import lab3.crypto.SHA256;
import lab3.crypto.SHA256Hash;
import lab3.crypto.elgamal.PublicKey;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class FileInfo {
    public final SHA256Hash contentsHash;
    public final String name;
    public final PublicKey owner;
    public final File location;

    public FileInfo(SHA256Hash hash, String name, PublicKey owner, File location) {
        assert location.exists();
        this.contentsHash = hash;
        this.name = name;
        this.owner = owner;
        this.location = location;
    }

    public SHA256Hash hash() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        owner.store(baos);
        SHA256.hash(name.getBytes()).store(baos);
        contentsHash.store(baos);
        return SHA256.hash(baos.toByteArray());
    }
}
