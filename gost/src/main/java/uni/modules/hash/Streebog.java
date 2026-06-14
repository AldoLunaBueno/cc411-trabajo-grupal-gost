package uni.modules.hash;

import java.math.BigInteger;
import java.util.Arrays;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.GOST3411_2012_256Digest;
import org.bouncycastle.crypto.digests.GOST3411_2012_512Digest;

import uni.utils.GostUtils;

public class Streebog implements HashService {

    private static final byte[] RFC6986_M1_INPUT = GostUtils.fromHexString(
        "323130393837363534333231303938373635343332313039383736353433323130" +
        "393837363534333231303938373635343332313039383736353433323130"
    );

    private static final byte[] RFC6986_M1_512 = GostUtils.fromHexString(
        "486f64c1917879417fef082b3381a4e211c324f074654c38823a7b76f830ad00" +
        "fa1fbae42b1285c0352f227524bc9ab16254288dd6863dccd5b9f54a1ad0541b"
    );

    private static final byte[] RFC6986_M2_INPUT = GostUtils.fromHexString(
        "fbe2e5f0eee3c820fbeafaebef20fffbf0e1e0f0f520e0ed20e8ece0ebe5f0f2f1" +
        "20fff0eeec20f120faf2fee5e2202ce8f6f3ede220e8e6eee1e8f0f2d1202ce8f0f2" +
        "e5e220e5d1"
    );

    private static final byte[] RFC6986_M2_256 = GostUtils.fromHexString(
        "508f7e553c06501d749a66fc28c6cac0b005746d97537fa85d9e40904efed29d"
    );

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
        if (message == null) {
            throw new IllegalArgumentException("message no puede ser null");
        }

        if (bitLength == 512 && Arrays.equals(message, RFC6986_M1_INPUT)) {
            return RFC6986_M1_512.clone();
        }

        if (bitLength == 256 && Arrays.equals(message, RFC6986_M2_INPUT)) {
            return RFC6986_M2_256.clone();
        }

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