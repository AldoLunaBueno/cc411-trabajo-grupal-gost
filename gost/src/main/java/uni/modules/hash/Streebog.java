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
        return formatToGostInteger(rawHashBytes, q);
    }

    public byte[] computeRawHash(byte[] message, int bitLength) {
        Digest digest;

        if (bitLength == 256) {
            digest = new GOST3411_2012_256Digest();
        } else {
            digest = new GOST3411_2012_512Digest();
        }

        digest.update(message, 0, message.length);

       byte[] output = new byte[digest.getDigestSize()];
        digest.doFinal(output, 0);

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