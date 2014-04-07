package lab3.crypto.elgamal;

import lab3.bytes.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Optional;

public class PublicKey implements Storable {
    public final BigInteger key;

    public PublicKey(BigInteger key) {
        this.key = key;
    }

    public static Optional<PublicKey> loadFrom(InputStream is) {
        return new ByteArrayLoader(byteLength()).load(is).map(BigIntFromBytes::construct).map(PublicKey::new);
    }

    @Override
    public boolean store(OutputStream os) {
        byte[] bytes = key.toByteArray();
        byte[] bytes1 = new byte[128];
        if (bytes.length > 128) {
            System.arraycopy(bytes, bytes.length - 128, bytes1, 0, 128);
        } else {
            System.arraycopy(bytes, 0, bytes1, 128 - bytes.length, bytes.length);
        }
        return new ByteArrayStorer(bytes1).store(os);
    }

    public static int byteLength() {
        return 128;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PublicKey publicKey = (PublicKey) o;

        return key.equals(publicKey.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    public String toShortString() {
        return toString().substring(0, 10);
    }

    @Override
    public String toString() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        store(baos);
        return Hex.toHex(baos.toByteArray());
    }
}
