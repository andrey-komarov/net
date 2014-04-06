package ru.ifmo.ctddev.komarov.net.lab3.bytes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Optional;

public class IntLoader {
    public static Optional<Integer> loadFrom(InputStream is) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        for (int i = 0; i < 4; i++) {
            try {
                int r = is.read();
                bb.put((byte) r);
            } catch (IOException e) {
                return Optional.empty();
            }
        }
        return Optional.of(bb.getInt());
    }
}
