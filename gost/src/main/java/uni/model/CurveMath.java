package uni.model;

import java.math.BigInteger;

public class CurveMath {

    private static final BigInteger ONE = BigInteger.ONE;
    private static final BigInteger TWO = BigInteger.TWO;
    private static final BigInteger THREE = BigInteger.valueOf(3L);

    public static Point add(Point p1, Point p2, DomainParameters params) {
        if (p1 == null) return p2;
        if (p2 == null) return p1;

        if (p1.equals(p2)) {
            return doublePoint(p1, params);
        }

        if (p1.x.equals(p2.x)) {
            return null;
        }

        BigInteger modulus = params.p;
        BigInteger dy = p1.y.subtract(p2.y).mod(modulus);
        BigInteger dx = p1.x.subtract(p2.x).mod(modulus);

        if (dx.equals(BigInteger.ZERO)) {
            return null;
        }

        BigInteger m = dy.multiply(dx.modInverse(modulus)).mod(modulus);
        BigInteger xr = m.multiply(m).subtract(p1.x).subtract(p2.x).mod(modulus);
        BigInteger yr = m.multiply(p1.x.subtract(xr)).subtract(p1.y).mod(modulus);

        return new Point(xr, yr);
    }

    public static Point doublePoint(Point p, DomainParameters params) {
        if (p == null) return null;

        BigInteger modulus = params.p;
        if (p.y.mod(modulus).equals(BigInteger.ZERO)) {
            return null;
        }

        BigInteger numerator = THREE.multiply(p.x.pow(2)).add(params.a).mod(modulus);
        BigInteger denominator = TWO.multiply(p.y).mod(modulus);

        if (denominator.equals(BigInteger.ZERO)) {
            return null;
        }

        BigInteger m = numerator.multiply(denominator.modInverse(modulus)).mod(modulus);
        BigInteger xr = m.multiply(m).subtract(TWO.multiply(p.x)).mod(modulus);
        BigInteger yr = m.multiply(p.x.subtract(xr)).subtract(p.y).mod(modulus);

        return new Point(xr, yr);
    }

    public static Point multiply(Point p, BigInteger n, DomainParameters params) {
        if (p == null || n == null || params == null) return null;
        if (n.signum() < 0) {
            throw new IllegalArgumentException("n no puede ser negativo");
        }
        if (n.equals(BigInteger.ZERO)) {
            return null;
        }

        Point result = null;
        Point addend = p;
        BigInteger k = n;

        while (k.signum() > 0) {
            if (k.testBit(0)) {
                result = add(result, addend, params);
            }
            addend = doublePoint(addend, params);
            k = k.shiftRight(1);
        }

        return result;
    }

    public static boolean isPointOnCurve(Point P, DomainParameters params) {
        if (P == null) return true;
        if (params == null || params.p == null || params.a == null || params.b == null) return false;

        BigInteger modulus = params.p;

        BigInteger left = P.y.mod(modulus).multiply(P.y.mod(modulus)).mod(modulus);
        BigInteger right = P.x.mod(modulus).pow(3)
                .add(params.a.multiply(P.x.mod(modulus)))
                .add(params.b)
                .mod(modulus);

        return left.equals(right);
    }
}