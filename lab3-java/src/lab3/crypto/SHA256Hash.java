package lab3.crypto;

import lab3.bytes.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;

public class SHA256Hash implements Storable, Comparable<SHA256Hash> {
    private final byte[] hash;

    public SHA256Hash(byte[] hash) {
        assert hash.length == byteLength();
        this.hash = hash.clone();
    }

    @Override
    public String toString() {
        return Hex.toHex(hash);
    }

    public BigInteger toBigInteger() {
        return BigIntFromBytes.construct(hash);
    }

    public static int byteLength() {
        return 32;
    }

    public static Optional<SHA256Hash> loadFrom(InputStream is) {
        return new ByteArrayLoader(byteLength()).load(is).map(SHA256Hash::new);
    }

    @Override
    public boolean store(OutputStream os) {
        return new ByteArrayStorer(hash).store(os);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SHA256Hash that = (SHA256Hash) o;

        if (!Arrays.equals(hash, that.hash)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(hash);
    }

    @Override
    public int compareTo(SHA256Hash other) {
        for (int i = 0; i < byteLength(); i++) {
            if (hash[i] != other.hash[i]) {
                return hash[i] - other.hash[i];
            }
        }
        return 0;
    }
}
