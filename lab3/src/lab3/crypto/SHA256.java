package lab3.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Supplier;

public class SHA256 {
    private final static byte[] buf = new byte[1 << 16];
    private final static Supplier<MessageDigest> SHA256s = () -> {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new ExceptionInInitializerError(e);
        }
    };

    public static SHA256Hash hash(InputStream is) throws IOException {
        MessageDigest sha256 = SHA256s.get();
        int r;
        int len = 0;
        while ((r = is.read(buf)) != -1) {
            sha256.update(buf, 0, r);
            len += r;
        }
        return new SHA256Hash(sha256.digest());
    }

    public static SHA256Hash hash(byte[] bytes) {
        return new SHA256Hash(SHA256s.get().digest(bytes));
    }
}
