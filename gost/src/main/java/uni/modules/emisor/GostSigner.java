package uni.modules.emisor;

import java.math.BigInteger;

import uni.model.CurveMath;
import uni.model.DomainParameters;
import uni.model.Point;
import uni.model.Signature;

public class GostSigner implements SignatureGenerator {

    @Override
    public Signature sign(BigInteger e, BigInteger d, BigInteger k, DomainParameters params) {
        if (e == null || d == null || k == null || params == null || params.P == null || params.q == null) {
            throw new IllegalArgumentException("Parámetros inválidos para la firma");
        }

        BigInteger q = params.q;

        if (k.signum() <= 0 || k.compareTo(q) >= 0) {
            throw new IllegalArgumentException("k debe cumplir 0 < k < q");
        }

        e = e.mod(q);
        if (e.equals(BigInteger.ZERO)) {
            e = BigInteger.ONE;
        }

        Point C = CurveMath.multiply(params.P, k, params);
        if (C == null) {
            throw new IllegalStateException("No se pudo calcular kP");
        }

        BigInteger r = C.x.mod(q);
        if (r.equals(BigInteger.ZERO)) {
            throw new IllegalArgumentException("r = 0");
        }

        BigInteger s = r.multiply(d).add(k.multiply(e)).mod(q);
        if (s.equals(BigInteger.ZERO)) {
            throw new IllegalArgumentException("s = 0");
        }

        return new Signature(r, s);
    }
}