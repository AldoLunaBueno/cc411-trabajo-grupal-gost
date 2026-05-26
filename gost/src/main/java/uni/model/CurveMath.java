package uni.model;

import java.math.BigInteger;

/**
 * Clase centralizada para las operaciones algebraicas sobre la Curva Elíptica.
 * Todo el equipo debe usar estos métodos en lugar de programar los suyos.
 */
public class CurveMath {

    /**
     * Suma de puntos: R = P + Q
     * Se usa cuando los puntos son distintos.
     */
    public static Point add(Point p1, Point p2, DomainParameters params) {
        throw new UnsupportedOperationException("Falta implementar suma de puntos");
    }

    /**
     * Duplicación de puntos: R = 2P
     * Se usa cuando se suma un punto consigo mismo.
     */
    public static Point doublePoint(Point p, DomainParameters params) {
        throw new UnsupportedOperationException("Falta implementar duplicación de puntos");
    }

    /**
     * Multiplicación escalar: R = n * P
     * Implementa el algoritmo Double-and-Add para eficiencia extrema.
     */
    public static Point multiply(Point P, BigInteger n, DomainParameters params) {
        throw new UnsupportedOperationException("Falta implementar multiplicación escalar de puntos");
    }
}