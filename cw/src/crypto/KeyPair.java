package crypto;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class KeyPair {
    public static Random RND = new SecureRandom();

    public final PrivateKey privateKey;
    public final PublicKey publicKey;
    public final Params params;

    public KeyPair(Params params) {
        this.params = params;
        privateKey = new PrivateKey(BigRandom.randFromTo(RND, BigInteger.valueOf(2), params.p.subtract(BigInteger.ONE)));
        publicKey = new PublicKey(params.g.modPow(privateKey.key, params.p));
    }
}
