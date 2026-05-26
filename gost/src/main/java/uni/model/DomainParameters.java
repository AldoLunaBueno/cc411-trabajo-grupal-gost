package uni.model;

import java.math.BigInteger;

// Representa los parámetros públicos de la curva (p, a, b, q, P)
// Ecuación de la curva elíptica sobre un cuerpo finito:
// y^2 = x^3 + a*x + b (mod p)
public class DomainParameters {
    public final BigInteger p;  // primo del cuerpo
    public final BigInteger a;  // coef.
    public final BigInteger b;  // coef.
    public final BigInteger q;  // orden del grupo
    public final Point P;       // punto base o generador (x, y)

    public DomainParameters(BigInteger p, BigInteger a, BigInteger b, BigInteger q, Point P) {
        this.p = p;
        this.a = a;
        this.b = b;
        this.q = q;
        this.P = P;
    }
}