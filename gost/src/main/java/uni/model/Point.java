package uni.model;

import java.math.BigInteger;

// Representa un punto en la curva elíptica
public class Point {
    public final BigInteger x;
    public final BigInteger y;

    public Point(BigInteger x, BigInteger y) {
        this.x = x;
        this.y = y;
    }
}