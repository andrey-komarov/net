package lab3.structs;

import lab3.bytes.IntLoader;
import lab3.bytes.Storable;
import lab3.crypto.SHA256Hash;
import lab3.crypto.elgamal.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class RevisionFiles implements Storable {
    public final Signature signature;
    public final int version;
    public final List<SHA256Hash> files;

    public RevisionFiles(Signature signature, int version, List<SHA256Hash> files) {
        this.signature = signature;
        this.version = version;
        this.files = new ArrayList<>(files);
        Collections.sort(this.files);
    }

    public RevisionFiles(Signer signer, int version, List<SHA256Hash> files) {
        this(new Object(){
            Signature sign() {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Storable.storeInt(version, baos);
                Storable.storeInt(files.size(), baos);
                files.forEach(f -> f.store(baos));
                return signer.sign(baos.toByteArray());
            }
        }.sign(), version, files);
    }

    @Override
    public boolean store(OutputStream os) {
        if (!signature.store(os)) {
            return false;
        }
        if (!Storable.storeInt(version, os)) {
            return false;
        }
        if (!Storable.storeInt(files.size(), os)) {
            return false;
        }
        for (SHA256Hash hash : files) {
            if (!hash.store(os)) {
                return false;
            }
        }
        return true;
    }

    public boolean verify(Params p, PublicKey key) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Storable.storeInt(version, baos);
        Storable.storeInt(files.size(), baos);
        files.forEach(f -> f.store(baos));
        Verifier v = new Verifier(p);
        return v.verify(signature, key, baos.toByteArray());
    }

    public static Optional<RevisionFiles> loadFrom(InputStream is) {
        Optional<Signature> oSig = Signature.loadFrom(is);
        if (!oSig.isPresent()) {
            return Optional.empty();
        }
        Signature sig = oSig.get();

        Optional<Integer> oVer = IntLoader.loadFrom(is);
        if (!oVer.isPresent()) {
            return Optional.empty();
        }
        int ver = oVer.get();

        Optional<Integer> oLen = IntLoader.loadFrom(is);
        if (!oLen.isPresent()) {
            return Optional.empty();
        }
        int len = oLen.get();

        List<SHA256Hash> files = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            Optional<SHA256Hash> oHash = SHA256Hash.loadFrom(is);
            if (!oHash.isPresent()) {
                return Optional.empty();
            }
            files.add(oHash.get());
        }
        return Optional.of(new RevisionFiles(sig, ver, files));
    }

    public int byteLength() {
        return Signature.byteLength() + 4 + 4 + SHA256Hash.byteLength() * files.size();
    }

    public static final Comparator<RevisionFiles> BY_VERSION = (rf1, rf2) -> rf1.version - rf2.version;

    @Override
    public String toString() {
        ArrayList<String> filenames = new ArrayList<>();
        files.forEach(f -> filenames.add(f.toString()));
        return "Files[" + version + " : " + filenames + "]";
    }
}
