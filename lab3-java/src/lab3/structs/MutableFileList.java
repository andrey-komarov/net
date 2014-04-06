package lab3.structs;

import lab3.crypto.SHA256;
import lab3.crypto.SHA256Hash;
import lab3.crypto.elgamal.KeyPair;
import lab3.crypto.elgamal.Signer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MutableFileList {
    static class PairFileHash {
        public final File file;
        public final SHA256Hash hash;

        public PairFileHash(File file) throws IOException {
            this.file = file;
            hash = SHA256.hash(new FileInputStream(file));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PairFileHash that = (PairFileHash) o;

            if (!file.equals(that.file)) return false;
            if (!hash.equals(that.hash)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = file.hashCode();
            result = 31 * result + hash.hashCode();
            return result;
        }
    }

    private final KeyPair keys;
    private int version = 1;
    private Set<PairFileHash> files = new HashSet<>();

    public MutableFileList(KeyPair keys) {
        this.keys = keys;
    }

    public void addFile(File file) throws IOException {
        PairFileHash p = new PairFileHash(file);
        if (!files.contains(p)) {
            version++;
            files.add(p);
        }
    }

    public void removeFile(File file) throws IOException {
        PairFileHash p = new PairFileHash(file);
        if (!files.contains(p)) {
            version++;
            files.remove(p);
        }
    }

    public RevisionFiles toRevisionFiles() {
        ArrayList<SHA256Hash> fileHashes = new ArrayList<>();
        files.forEach(p -> fileHashes.add(new FileInfo(p.hash, p.file.getName(), keys.publicKey, p.file).hash()));
        Collections.sort(fileHashes);
        return new RevisionFiles(new Signer(keys.params, keys.privateKey), version, fileHashes);
    }
}
