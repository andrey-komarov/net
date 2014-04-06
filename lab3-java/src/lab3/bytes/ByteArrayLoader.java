package lab3.bytes;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class ByteArrayLoader implements Loadable<byte[]> {
    private final int length;

    public ByteArrayLoader(int length) {
        this.length = length;
    }

    @Override
    public Optional<byte[]> load(InputStream is) {
        byte[] res = new byte[length];
        for (int i = 0; i < length; i++) {
            try {
                int r = is.read();
                if (r == -1) {
                    return Optional.empty();
                }
                res[i] = (byte) r;
            } catch (IOException e) {
                return Optional.empty();
            }
        }
        return Optional.of(res);
    }
}
