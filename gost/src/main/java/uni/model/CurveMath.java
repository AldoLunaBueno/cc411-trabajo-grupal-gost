package uni.model;

import java.math.BigInteger;

/**
 * Clase centralizada para las operaciones algebraicas sobre la Curva Elíptica.
 * Todo el equipo debe usar estos métodos en lugar de programar los suyos.
 */
public class CurveMath {

    private static final BigInteger ONE = BigInteger.ONE;
    private static final BigInteger TWO = BigInteger.TWO;
    private static final BigInteger THREE = BigInteger.valueOf(3L);

    /**
     * Suma de puntos: R = P + Q
     * Se usa cuando los puntos son distintos.
     */
    public static Point add(Point p1, Point p2, DomainParameters params) {
        if (p1 != null && p2 == null) return p1;
        else if (p1 == null && p2 != null) return p2;
        else if (p1 == null && p2 == null) return null;
        else if (p1.x.equals(p2.x)) return null;
        else if (p1.equals(p2)) return doublePoint(p1, params); // la recta es tangente, no secante

        BigInteger dy = p1.y.subtract(p2.y).mod(params.p);
        BigInteger dx = p1.x.subtract(p2.x).mod(params.p);
        if (!dx.gcd(params.p).equals(ONE)) return null;
        BigInteger m = dy.multiply(dx.modInverse(params.p)).mod(params.p);
        BigInteger xr = m.pow(2).subtract(p1.x).subtract(p2.x).mod(params.p);
        BigInteger yr = p1.y.add(m.multiply(xr.subtract(p1.x))).mod(params.p);

        return new Point(xr, yr.negate().mod(params.p));
    }

    /**
     * Duplicación de puntos: R = 2P
     * Se usa cuando se suma un punto consigo mismo.
     */
    public static Point doublePoint(Point p, DomainParameters params) {
        if (p == null) return null;
        if (!p.y.gcd(params.p).equals(ONE)) return null;

        BigInteger m = THREE.multiply(p.x.pow(2)).add(params.a).multiply(TWO.multiply(p.y).modInverse(params.p)).mod(params.p);
        BigInteger xr = m.pow(2).subtract(p.x).subtract(p.x).mod(params.p);
        BigInteger yr = p.y.add(m.multiply(xr.subtract(p.x))).mod(params.p);

        return new Point(xr, yr.negate().mod(params.p));
    }

    /**
     * Multiplicación escalar: R = n * P
     * Implementa el algoritmo Double-and-Add para eficiencia extrema.
     */
    public static Point multiply(Point p, BigInteger n, DomainParameters params) {
        Point R = null;
        Point currPow = new Point(p.x, p.y);

        int numBits = n.bitLength();
        for (int i = 0; i < numBits; i++) {
            if (n.testBit(i)) {
                R = add(R, currPow, params);
            }
            currPow = doublePoint(currPow, params);
        }
        return R;
    }

    public static boolean isPointOnCurve(Point P, DomainParameters params) {
        return false;
    }
}