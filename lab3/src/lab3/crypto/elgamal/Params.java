package lab3.crypto.elgamal;

import java.math.BigInteger;

public class Params {
    public static final Params DEFAULT = new Params(
            BigInteger.valueOf(65537),
            new BigInteger("a56196d86790c6c1c12ce336a362815ca663d1661839145d3ac4cbd3ad9c810a245ef972b9fb88210b8a5c592f9dad013f185091064b7c27fc1e2e075b3392c013e5a4601e9fe6c64d34f2a33f6eb5c8bd760b35be1cf2fed97433c81239f3021de62dfdb1fb91ed785cc8dacfe816b2dcf2505f87608577a5a191f3078f0c3d", 16)
    );

    public final BigInteger g, p;

    public Params(BigInteger g, BigInteger p) {
        this.g = g;
        this.p = p;
    }
}
