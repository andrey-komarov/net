package ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal;

import ru.ifmo.ctddev.komarov.net.lab3.bytes.ByteArrayLoader;
import ru.ifmo.ctddev.komarov.net.lab3.bytes.ByteArrayStorer;
import ru.ifmo.ctddev.komarov.net.lab3.bytes.Hex;
import ru.ifmo.ctddev.komarov.net.lab3.bytes.Storable;

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
        return new ByteArrayLoader(byteLength()).load(is).map(BigInteger::new).map(PublicKey::new);
    }

    @Override
    public boolean store(OutputStream os) {
        byte[] bytes = key.toByteArray();
        int start = bytes.length == 129 ? 1 : 0;
        return new ByteArrayStorer(bytes, start, bytes.length).store(os);
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

    @Override
    public String toString() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        store(baos);
        return Hex.toHex(baos.toByteArray());
    }
}
