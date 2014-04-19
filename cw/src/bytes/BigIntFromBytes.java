package bytes;

import java.math.BigInteger;

public class BigIntFromBytes {
    public static BigInteger construct(byte[] bytes) {
        byte[] bytes1 = new byte[bytes.length + 1];
        System.arraycopy(bytes, 0, bytes1, 1, bytes.length);
        return new BigInteger(bytes1);
    }
}
