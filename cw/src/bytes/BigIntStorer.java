package bytes;

import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Arrays;

public class BigIntStorer implements Storable {
    private final BigInteger n;
    private final int length;

    public BigIntStorer(BigInteger n, int length) {
        this.n = n;
        this.length = length;
    }

    @Override
    public boolean store(OutputStream os) {
        byte[] bn = n.toByteArray();
        byte[] bytes;
        if (bn.length > length) {
            bytes = Arrays.copyOfRange(bn, bn.length - length, bn.length);
        } else {
            bytes = new byte[length];
            System.arraycopy(bn, 0, bytes, length - bn.length, bn.length);
        }
        return new ByteArrayStorer(bytes).store(os);
    }
}
