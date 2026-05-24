package uni.modules.emisor;

import java.math.BigInteger;
import uni.model.Signature;
import uni.model.DomainParameters;

public interface SignatureGenerator {
    // Genera la firma (r, s) dado el mensaje (ya como entero e) y la clave privada d
    Signature sign(BigInteger e, BigInteger d, DomainParameters params);
}