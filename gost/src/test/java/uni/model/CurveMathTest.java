package uni.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

public class CurveMathTest {
    private BigInteger p = BigInteger.valueOf(97L);
    private BigInteger a = BigInteger.valueOf(2L);
    private BigInteger b = BigInteger.valueOf(3L);
    private DomainParameters param = new DomainParameters(p, a, b, null, null);

    @Test
    void testAdd() {

        BigInteger xp = BigInteger.valueOf(17L);
        BigInteger yp = BigInteger.valueOf(10);
        Point P = new Point(xp, yp);

        BigInteger xq = BigInteger.valueOf(95L);
        BigInteger yq = BigInteger.valueOf(31L);
        Point Q = new Point(xq, yq);

        BigInteger xrExp = BigInteger.valueOf(1L);
        BigInteger yrExp = BigInteger.valueOf(54L);
        Point RExpected = new Point(xrExp, yrExp);

        Point RActual = CurveMath.add(P, Q, param);
        
        assertEquals(RExpected, RActual);
    }

    @Test
    void testDoublePoint() {

        BigInteger xp = BigInteger.valueOf(3L);
        BigInteger yp = BigInteger.valueOf(6L);
        Point P = new Point(xp, yp);

        BigInteger xqExp = BigInteger.valueOf(80L);
        BigInteger yqExp = BigInteger.valueOf(10L);
        Point QExpected = new Point(xqExp, yqExp);

        Point QActual = CurveMath.doublePoint(P, param);
        
        assertEquals(QExpected, QActual);

    }

    @Test
    void testMultiply() {
        
        BigInteger n = BigInteger.valueOf(42L);

        BigInteger xp = BigInteger.valueOf(24L);
        BigInteger yp = BigInteger.valueOf(2L);
        Point P = new Point(xp, yp);

        BigInteger xqExp = BigInteger.valueOf(84L);
        BigInteger yqExp = BigInteger.valueOf(37L);
        Point QExpected = new Point(xqExp, yqExp);

        Point QActual = CurveMath.multiply(P, n, param);
        
        assertEquals(QExpected, QActual);
    }
}
