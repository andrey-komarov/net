package crypto;

import java.math.BigInteger;

public class PrivateKey {
    public final BigInteger key;

    public PrivateKey(BigInteger key) {
        this.key = key;
    }
}
