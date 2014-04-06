package ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal;

import ru.ifmo.ctddev.komarov.net.lab3.crypto.BigRandom;
import ru.ifmo.ctddev.komarov.net.lab3.crypto.SHA256;
import ru.ifmo.ctddev.komarov.net.lab3.crypto.SHA256Hash;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

public class Signer {
    private final static Random RND = new SecureRandom();

    private final Params params;

    public Signer(Params params) {
        this.params = params;
    }

    private Signature sign(PrivateKey key, SHA256Hash hash) {
        while (true) {
            BigInteger pMinus1 = params.p.subtract(ONE);
            BigInteger k = BigRandom.randFromTo(RND, ONE, pMinus1);
            if (!k.gcd(pMinus1).equals(ONE)) {
                continue;
            }
            BigInteger r = params.g.modPow(k, params.p);
            BigInteger s = hash.toBigInteger().subtract(key.key.multiply(r))
                    .multiply(k.modInverse(pMinus1))
                    .mod(pMinus1);
            if (!s.equals(ZERO)) {
                return new Signature(r, s);
            }
        }
    }

    public Signature sign(PrivateKey key, InputStream is) throws IOException {
        return sign(key, SHA256.hash(is));
    }

    public Signature sign(PrivateKey key, byte[] bytes) {
        return sign(key, SHA256.hash(bytes));
    }
}
