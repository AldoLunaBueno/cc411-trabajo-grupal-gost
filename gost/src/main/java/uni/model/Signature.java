package uni.model;

import java.math.BigInteger;

// Representa la firma digital desglosada en sus dos componentes r y s
public class Signature {
    public final BigInteger r;
    public final BigInteger s;

    public Signature(BigInteger r, BigInteger s) {
        this.r = r;
        this.s = s;
    }
}