package uni.modules.emisor;

import java.math.BigInteger;

import uni.model.CurveMath;
import uni.model.Point;
import uni.model.DomainParameters;
import uni.model.Signature;

// Jharvy
public class GostSigner implements SignatureGenerator {
    @Override
    public Signature sign(BigInteger e, BigInteger d, BigInteger k, DomainParameters params) {
        BigInteger q = params.q;

        // Paso 1: e ya viene como entero reducido por el hash
        // Aseguramos que e esté en el rango [0, q)
        e = e.mod(q);
        if (e.signum() == 0) e = BigInteger.ONE;

        // Paso 2: C = k * P
        Point C = CurveMath.multiply(params.P, k, params);
        if (C == null) throw new IllegalStateException("Punto C es el infinito (multiplicación fallida)");

        // Paso 3: r = x_C mod q
        BigInteger r = C.x.mod(q);
        if (r.signum() == 0) throw new IllegalStateException("Valor r == 0; elegir otro k");

        // Paso 4: s = (r*d + k*e) mod q
        BigInteger s = r.multiply(d).add(k.multiply(e)).mod(q);
        if (s.signum() == 0) throw new IllegalStateException("Valor s == 0; elegir otro k");

        // Paso 5: retornar la firma (r, s)
        return new Signature(r, s);
    }
    
}
