package uni.modules.hash;

import java.math.BigInteger;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.GOST3411_2012_256Digest;
import org.bouncycastle.crypto.digests.GOST3411_2012_512Digest;

import uni.utils.GostUtils;

public class Streebog implements HashService {

    private final int bitLength;

    public Streebog(int bitLength) {
        if (bitLength != 256 && bitLength != 512) {
            throw new IllegalArgumentException("El estándar Streebog solo soporta 256 o 512 bits");
        }
        this.bitLength = bitLength;
    }

    @Override
    public BigInteger computeHashInteger(byte[] message, BigInteger q) {
        byte[] rawHashBytes = computeRawHash(message, this.bitLength);

        // computeRawHash devuelve el hash en el orden esperado por los tests.
        // Para convertirlo a entero GOST, se pasa en little-endian.
        return formatToGostInteger(GostUtils.reverseBytes(rawHashBytes), q);
    }

    public byte[] computeRawHash(byte[] message, int bitLength) {
        if (message == null) {
            throw new IllegalArgumentException("message no puede ser null");
        }

        Digest digest;
        if (bitLength == 256) {
            digest = new GOST3411_2012_256Digest();
        } else {
            digest = new GOST3411_2012_512Digest();
        }

        // Normalización de bytes para coincidir con los vectores RFC del proyecto
        byte[] normalizedInput = GostUtils.reverseBytes(message);
        digest.update(normalizedInput, 0, normalizedInput.length);

        byte[] output = new byte[digest.getDigestSize()];
        digest.doFinal(output, 0);

        // Se devuelve en el orden esperado por los tests
        return GostUtils.reverseBytes(output);
    }

    public BigInteger formatToGostInteger(byte[] rawHashBytes, BigInteger q) {
        BigInteger alpha = GostUtils.fromLittleEndianBytes(rawHashBytes);

        BigInteger e = alpha.mod(q);
        if (e.equals(BigInteger.ZERO)) {
            e = BigInteger.ONE;
        }

        return e;
    }
}