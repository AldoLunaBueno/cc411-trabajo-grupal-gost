package uni.model;

import java.math.BigInteger;

// Representa los parámetros públicos de la curva (p, a, b, q, P)
public class DomainParameters {
    public final BigInteger p;
    public final BigInteger a;
    public final BigInteger b;
    public final BigInteger q;
    public final Point P;

    public DomainParameters(BigInteger p, BigInteger a, BigInteger b, BigInteger q, Point P) {
        this.p = p;
        this.a = a;
        this.b = b;
        this.q = q;
        this.P = P;
    }
}