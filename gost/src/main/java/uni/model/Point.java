package uni.model;

import java.math.BigInteger;
import java.util.Objects;

// Representa un punto en la curva elíptica
public class Point {
    public final BigInteger x;
    public final BigInteger y;

    public Point(BigInteger x, BigInteger y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Point p = (Point) obj;
        return this.x.equals(p.x) && this.y.equals(p.y);
    }

    @Override
    public int hashCode() {
        // Genera un hash único combinando los hashes internos de los BigInteger x e y
        return Objects.hash(x, y);
    }
}