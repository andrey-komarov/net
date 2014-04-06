package ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal;

import java.math.BigInteger;

public class PrivateKey {
    public final BigInteger key;

    public PrivateKey(BigInteger key) {
        this.key = key;
    }
}
