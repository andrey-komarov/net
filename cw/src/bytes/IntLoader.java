package bytes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Optional;

public class IntLoader {
    public static Optional<Integer> loadFrom(InputStream is) {
        byte[] ba = new byte[4];
        ByteBuffer bb = ByteBuffer.allocate(4);
        for (int i = 0; i < 4; i++) {
            try {
                int r = is.read();
                ba[i] = (byte) r;
            } catch (IOException e) {
                return Optional.empty();
            }
        }
        return Optional.of(ByteBuffer.wrap(ba).getInt());
    }
}
