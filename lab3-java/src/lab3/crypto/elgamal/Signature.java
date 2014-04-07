package lab3.crypto.elgamal;

import lab3.bytes.BigIntFromBytes;
import lab3.bytes.BigIntStorer;
import lab3.bytes.ByteArrayLoader;
import lab3.bytes.Storable;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Optional;

public class Signature implements Storable {
    public final BigInteger r, s;

    public Signature(BigInteger r, BigInteger s) {
        this.r = r;
        this.s = s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Signature signature = (Signature) o;

        if (!r.equals(signature.r)) return false;
        if (!s.equals(signature.s)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = r.hashCode();
        result = 31 * result + s.hashCode();
        return result;
    }

    public static Optional<Signature> loadFrom(InputStream is) {
        return new ByteArrayLoader(128).load(is).map(BigIntFromBytes::construct).flatMap(rr ->
                        new ByteArrayLoader(128).load(is).map(BigIntFromBytes::construct).map(ss -> new Signature(rr, ss))
        );
    }

    public static int byteLength() {
        return 256;
    }

    @Override
    public boolean store(OutputStream os) {
        return new BigIntStorer(r, 128).store(os)
                && new BigIntStorer(s, 128).store(os);
    }
}
