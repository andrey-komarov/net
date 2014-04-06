package ru.ifmo.ctddev.komarov.net.lab3.bytes;

import java.io.IOException;
import java.io.OutputStream;

public class ByteArrayStorer implements Storable {
    private final byte[] bytes;
    private final int left, right;

    public ByteArrayStorer(byte[] bytes) {
        this(bytes, 0, bytes.length);
    }

    public ByteArrayStorer(byte[] bytes, int left, int right) {
        this.bytes = bytes;
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean store(OutputStream os) {
        for (int i = left; i < right; i++) {
            try {
                os.write(bytes[i]);
            } catch (IOException e) {
                return false;
            }
        }
        return true;
    }
}
