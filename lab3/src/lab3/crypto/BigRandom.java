package lab3.crypto;

import java.math.BigInteger;
import java.util.Random;

public class BigRandom {

    // TODO Unfortunately, non-uniform
    public static BigInteger randTo(Random rnd, BigInteger max) {
        return new BigInteger(max.bitLength() * 2, rnd).mod(max);
    }

    public static BigInteger randFromTo(Random rnd, BigInteger left, BigInteger right) {
        assert left.compareTo(right) < 0;
        return left.add(randTo(rnd, right.subtract(left)));
    }
}
