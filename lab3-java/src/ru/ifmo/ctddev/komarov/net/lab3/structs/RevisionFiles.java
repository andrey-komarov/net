package ru.ifmo.ctddev.komarov.net.lab3.structs;

import ru.ifmo.ctddev.komarov.net.lab3.bytes.IntLoader;
import ru.ifmo.ctddev.komarov.net.lab3.bytes.Storable;
import ru.ifmo.ctddev.komarov.net.lab3.crypto.SHA256Hash;
import ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal.Signature;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class RevisionFiles implements Storable {
    public final Signature signature;
    public final int version;
    private final List<SHA256Hash> files;

    public RevisionFiles(Signature signature, int version, List<SHA256Hash> files) {
        this.signature = signature;
        this.version = version;
        this.files = new ArrayList<>(files);
        Collections.sort(this.files);
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
}
