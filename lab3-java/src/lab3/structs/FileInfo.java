package lab3.structs;

import lab3.bytes.ByteArrayStorer;
import lab3.bytes.Storable;
import lab3.crypto.SHA256;
import lab3.crypto.SHA256Hash;
import lab3.crypto.elgamal.PublicKey;

import java.io.*;

public class FileInfo implements Storable {
    public final SHA256Hash contentsHash;
    public final String name;
    public final PublicKey owner;
    public final File location;

    public FileInfo(SHA256Hash hash, String name, PublicKey owner, File location) {
        assert location.exists() : location.toString();
        this.contentsHash = hash;
        this.name = name;
        this.owner = owner;
        this.location = location;
    }

    public FileInfo(PublicKey owner, File location) throws IOException {
        this.owner = owner;
        this.location = location;
        FileInputStream fis = new FileInputStream(location);
        this.contentsHash = SHA256.hash(fis);
        this.name = location.getName();
    }

    public SHA256Hash hash() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        owner.store(baos);
        SHA256.hash(name.getBytes()).store(baos);
        contentsHash.store(baos);
        return SHA256.hash(baos.toByteArray());
    }

    @Override
    public boolean store(OutputStream os) {
        byte[] nameBytes = name.getBytes();
        boolean ok = owner.store(os);
        int len = (int) location.length();
        ok &= Storable.storeInt(nameBytes.length, os);
        ok &= Storable.storeInt(len, os);
        ok &= new ByteArrayStorer(nameBytes).store(os);

        try {
            FileInputStream is = new FileInputStream(location);
            byte[] buf = new byte[1 << 12];
            int r;
            while ((r = is.read(buf)) != -1 && ok) {
                os.write(buf, 0, r);
            }

//            for (int i = 0; i < len && ok; i++) {
//                os.write(is.read());
//            }
        } catch (IOException e) {
            return false;
        }
        return ok;
    }
}
