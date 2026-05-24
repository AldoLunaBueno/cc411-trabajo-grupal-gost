package uni.modules.hash;

import java.math.BigInteger;

public interface HashService {
    // Recibe el mensaje y devuelve el entero 'e' calculado a partir del hash
    BigInteger computeHashInteger(byte[] message, BigInteger q);
}