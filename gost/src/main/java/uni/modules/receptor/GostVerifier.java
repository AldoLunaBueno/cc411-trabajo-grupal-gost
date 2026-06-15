package uni.modules.receptor;

import java.math.BigInteger;

import uni.model.CurveMath;
import uni.model.DomainParameters;
import uni.model.Point;
import uni.model.Signature;

public class GostVerifier implements SignatureVerifier {

    @Override
    public boolean verify(Signature sig, BigInteger e, Point Q, DomainParameters params) {
        if (sig == null || e == null || Q == null || params == null || params.P == null || params.q == null) {
            return false;
        }

        BigInteger q = params.q;
        BigInteger r = sig.r;
        BigInteger s = sig.s;

        if (r == null || s == null) return false;

        if (r.signum() <= 0 || r.compareTo(q) >= 0) return false;
        if (s.signum() <= 0 || s.compareTo(q) >= 0) return false;

        e = e.mod(q);
        if (e.signum() == 0) e = BigInteger.ONE;

        BigInteger v = e.modInverse(q);
        BigInteger z1 = s.multiply(v).mod(q);
        BigInteger z2 = r.negate().multiply(v).mod(q);

        Point C = CurveMath.add(
                CurveMath.multiply(params.P, z1, params),
                CurveMath.multiply(Q, z2, params),
                params
        );

        if (C == null) {
            return false;
        }

        BigInteger R = C.x.mod(q);
        return R.equals(r);
    }
}