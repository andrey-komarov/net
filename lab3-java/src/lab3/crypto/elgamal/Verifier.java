package lab3.crypto.elgamal;

import lab3.crypto.SHA256;
import lab3.crypto.SHA256Hash;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

import static java.math.BigInteger.*;

public class Verifier {
    private final Params params;

    public Verifier(Params params) {
        this.params = params;
    }

    private boolean verify(Signature sig, PublicKey key, SHA256Hash hash) {
        if (!(ZERO.compareTo(sig.r) < 0 && sig.r.compareTo(params.p) < 0)) {
            return false;
        }
        if (!(ZERO.compareTo(sig.s) < 0 && sig.s.compareTo(params.p.subtract(ONE)) < 0)) {
            return false;
        }
        BigInteger lhs = params.g.modPow(hash.toBigInteger(), params.p);
        BigInteger rhs = key.key.modPow(sig.r, params.p).multiply(sig.r.modPow(sig.s, params.p)).mod(params.p);
        return lhs.equals(rhs);
    }

    public boolean verify(Signature sig, PublicKey key, InputStream is) throws IOException {
        return verify(sig, key, SHA256.hash(is));
    }

    public boolean verify(Signature sig, PublicKey key, byte[] bytes) {
        return verify(sig, key, SHA256.hash(bytes));
    }
}
