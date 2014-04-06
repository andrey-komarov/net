package ru.ifmo.ctddev.komarov.net.lab3.bytes;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public interface Storable {
    boolean store(OutputStream os);

    public static boolean storeInt(int i, OutputStream os) {
        try {
            os.write(ByteBuffer.allocate(4).putInt(i).array());
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
