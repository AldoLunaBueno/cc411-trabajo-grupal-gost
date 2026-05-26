package uni.modules.emisor;

import java.math.BigInteger;
import uni.model.Signature;
import uni.model.DomainParameters;
import java.security.SecureRandom;

public interface SignatureGenerator {

    /**
     * Método determinista.
     * Recibe 'k' como parámetro para hacer pruebas.
     * Genera la firma (r, s) dado el mensaje (ya como entero e) y la clave privada d.
     */
    Signature sign(BigInteger e, BigInteger d, BigInteger k, DomainParameters params);

    /**
     * Implementación por defecto
     * Genera su propio 'k' y delega el trabajo matemático al método determinista de arriba.
     */
    default Signature sign(BigInteger e, BigInteger d, DomainParameters params) {
        SecureRandom random = new SecureRandom();
        BigInteger k;
        
        // El estándar exige que k sea un entero aleatorio tal que 0 < k < q
        do {
            k = new BigInteger(params.q.bitLength(), random);
        } while (k.compareTo(BigInteger.ZERO) <= 0 || k.compareTo(params.q) >= 0);
        
        // Delega la ejecución a la implementación determninista
        return sign(e, d, k, params);
    }
}