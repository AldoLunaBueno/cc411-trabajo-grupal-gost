package uni.modules.hash;

import java.math.BigInteger;

import uni.utils.GostUtils;

// Benja
public class Streebog implements HashService {

    private final int bitLength;

    // Constructor que define el comportamiento de esta instancia
    public Streebog(int bitLength) {
        if (bitLength != 256 && bitLength != 512) {
            throw new IllegalArgumentException("El estándar Streebog solo soporta 256 o 512 bits");
        }
        this.bitLength = bitLength;
    }

    @Override
    public BigInteger computeHashInteger(byte[] message, BigInteger q) {
        // Usa la variable de instancia en lugar del número estático
        byte[] rawHashBytes = computeRawHash(message, this.bitLength);
        return formatToGostInteger(rawHashBytes, q);
    }

    public byte[] computeRawHash(byte[] message, int bitLength) {
        throw new UnsupportedOperationException("Unimplemented method 'computeRawHashHex'");
    }

    public BigInteger formatToGostInteger(byte[] rawHashBytes, BigInteger q) {
        // a) Convertir usando utilidades a BigEndian
        // b) Aplicar módulo q
        // c) Regla rusa (e=0 -> e=1)
        throw new UnsupportedOperationException("Unimplemented method 'formatToGostInteger'");
    }
}