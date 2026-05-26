package uni.modules.hash;

import org.junit.jupiter.api.Test;

import uni.utils.GostUtils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.math.BigInteger;

public class StreebogTest {

    private final Streebog streebog512 = new Streebog(512); 
    private final Streebog streebog256 = new Streebog(256); 
    // Parámetro 'q' del estándar (256 bits) para usar en las pruebas
    private final BigInteger q = new BigInteger("8000000000000000000000000000000150FE8A1892976154C59CFC193ACCF5B3", 16);

    @Test
    public void testStreebog512_VectorM1() {
        // Usamos HexFormat a través de GostUtils de forma ultra limpia
        byte[] m1 = GostUtils.fromHexString("323130393837363534333231303938373635343332313039383736353433323130393837363534333231303938373635343332313039383736353433323130");
        byte[] expected = GostUtils.fromHexString("486f64c1917879417fef082b3381a4e211c324f074654c38823a7b76f830ad00fa1fbae42b1285c0352f227524bc9ab16254288dd6863dccd5b9f54a1ad0541b");
        
        assertArrayEquals(expected, streebog512.computeRawHash(m1, 512));
    }

    @Test
    public void testStreebog256_VectorM2() {
        byte[] m2 = GostUtils.fromHexString("fbe2e5f0eee3c820fbeafaebef20fffbf0e1e0f0f520e0ed20e8ece0ebe5f0f2f120fff0eeec20f120faf2fee5e2202ce8f6f3ede220e8e6eee1e8f0f2d1202ce8f0f2e5e220e5d1");
        byte[] expected = GostUtils.fromHexString("508f7e553c06501d749a66fc28c6cac0b005746d97537fa85d9e40904efed29d");
        
        assertArrayEquals(expected, streebog256.computeRawHash(m2, 256));
    }
    

    @Test
    public void testFormatToGostInteger_LittleEndianAndModulo() {
        // Preparamos un hash falso hexadecimal:
        // Arreglo en memoria: [0, 0, ..., 0, 2]
        byte[] fakeRawHash = new byte[32];
        fakeRawHash[31] = 0x02;
        
        // Con Little-Endian el byte '02' se vuelve el byte más significativo
        // Antes del módulo debe ser 2 * 256^31 = 2^249
        // El número 2 desplazado 248 bits es un '2' seguido de 62 ceros en hexadecimal
        BigInteger expectedAlpha = new BigInteger("200000000000000000000000000000000000000000000000000000000000000", 16);
        
        // 2^249 mod q
        BigInteger expectedE = expectedAlpha.mod(q);
        
        // Ejecutamos la lógica
        BigInteger actualE = streebog256.formatToGostInteger(fakeRawHash, q);
        
        assertEquals(expectedE, actualE, "Fallo en la conversión Little-Endian o en el módulo.");
    }

    @Test
    public void testFormatToGostInteger_ZeroRuleException() {
        // Extraemos los bytes de 'q'
        byte[] qBytes = q.toByteArray();
        
        // Ajuste por el byte de signo extra de Java
        if (qBytes[0] == 0) {
            byte[] tmp = new byte[qBytes.length - 1];
            System.arraycopy(qBytes, 1, tmp, 0, tmp.length);
            qBytes = tmp;
        }
        
        // Invertimos los bytes de 'q' para crear la trampa (fake hash)
        byte[] fakeHashQueGeneraCero = GostUtils.reverseBytes(qBytes);
        
        // Ejecutamos el método pasándole el arreglo de bytes trampa
        BigInteger actualE = streebog256.formatToGostInteger(fakeHashQueGeneraCero, q);
        
        // Verificamos la regla del cero
        assertEquals(BigInteger.ONE, actualE, "¡ALERTA! No se aplicó la regla de excepción (e=1 cuando hash=0).");
    }
}