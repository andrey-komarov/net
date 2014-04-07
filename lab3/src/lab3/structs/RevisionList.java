package lab3.structs;

import lab3.bytes.IntLoader;
import lab3.bytes.Storable;
import lab3.crypto.SHA256;
import lab3.crypto.SHA256Hash;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class RevisionList implements Storable {
    private final List<Revision> revisions;

    private static final Comparator<Revision> BY_KEY = new Comparator<Revision>() {
        @Override
        public int compare(Revision r1, Revision r2) {
            return r1.key.key.compareTo(r2.key.key);
        }
    };

    public RevisionList(List<Revision> revisions) {
        this.revisions = new ArrayList<>(revisions);
        Collections.sort(this.revisions, BY_KEY);
    }

    public boolean storeWithoutLength(OutputStream os) {
        return revisions.stream().allMatch(r -> r.store(os));
    }


    @Override
    public boolean store(OutputStream os) {
        return Storable.storeInt(revisions.size(), os) && storeWithoutLength(os);
    }

    public static Optional<RevisionList> loadFrom(InputStream is) {
        Optional<Integer> olen = IntLoader.loadFrom(is);
        if (!olen.isPresent()) {
            return Optional.empty();
        }
        int len = olen.get();
        ArrayList<Revision> revisions = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            Optional<Revision> orev = Revision.loadFrom(is);
            if (orev.isPresent()) {
                revisions.add(orev.get());
            } else {
                return Optional.empty();
            }
        }
        return Optional.of(new RevisionList(revisions));
    }

    public SHA256Hash hash() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        storeWithoutLength(baos);
        return SHA256.hash(baos.toByteArray());
    }

    public int byteLength() {
        return 4 + Revision.byteLength() * revisions.size();
    }

    @Override
    public String toString() {
        return revisions.toString();
    }

    public List<Revision> getRevisions() {
        return revisions;
    }
}
