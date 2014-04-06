package ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal;

import ru.ifmo.ctddev.komarov.net.lab3.crypto.BigRandom;

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
