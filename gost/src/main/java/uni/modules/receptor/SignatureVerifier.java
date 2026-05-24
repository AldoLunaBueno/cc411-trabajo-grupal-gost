package uni.modules.receptor;

import java.math.BigInteger;

import uni.model.DomainParameters;
import uni.model.Point;
import uni.model.Signature;

public interface SignatureVerifier {
    // Verifica si la firma es válida usando la clave pública Q
    boolean verify(Signature sig, BigInteger e, Point Q, DomainParameters params);
}