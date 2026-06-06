package uni.modules.receptor;

import java.math.BigInteger;

import uni.model.CurveMath;
import uni.model.DomainParameters;
import uni.model.Point;
import uni.model.Signature;

// Melissa
public class GostVerifier implements SignatureVerifier {

    @Override
    public boolean verify(Signature sig, BigInteger e, Point Q, DomainParameters params) {
        BigInteger q = params.q;
        BigInteger r = sig.r;
        BigInteger s = sig.s;

        // Paso 1: r, s deben estar en (0, q)
        if (r.signum() <= 0 || r.compareTo(q) >= 0) return false;
        if (s.signum() <= 0 || s.compareTo(q) >= 0) return false;

        // Paso 2: e = h mod q; si es 0 se fuerza a 1
        e = e.mod(q);
        if (e.signum() == 0) e = BigInteger.ONE;

        // Paso 3: v = e^-1 mod q
        BigInteger v = e.modInverse(q);

        // Paso 4: z1 = s*v mod q, z2 = -r*v mod q
        BigInteger z1 = s.multiply(v).mod(q);
        BigInteger z2 = r.negate().multiply(v).mod(q);

        // Paso 5: C = z1*P + z2*Q
        Point C = CurveMath.add(
            CurveMath.multiply(params.P, z1, params),
            CurveMath.multiply(Q, z2, params),
            params
        );

        // Paso 6: R = x_C mod q
        BigInteger R = C.x.mod(q);

        // Paso 7: válida si R == r
        return R.equals(r);
    }

}
