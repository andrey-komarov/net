package ru.ifmo.ctddev.komarov.net.lab3.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {
    private final static MessageDigest SHA256;
    static {
        try {
            SHA256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    synchronized public static SHA256Hash hash(InputStream is) throws IOException {
        SHA256.reset();
        int r;
        while ((r = is.read()) != -1) {
            SHA256.update((byte)r);
        }
        return new SHA256Hash(SHA256.digest());
    }

    public static SHA256Hash hash(byte[] bytes) {
        return new SHA256Hash(SHA256.digest(bytes));
    }
}
