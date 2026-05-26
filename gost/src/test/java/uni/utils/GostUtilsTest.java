package uni.utils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import uni.utils.GostUtils;

public class GostUtilsTest {

    @Test
    public void testReverseBytes() {
        // Entrada: [1, 2, 3, 4]
        byte[] input = {0x01, 0x02, 0x03, 0x04};
        // Esperado: [4, 3, 2, 1]
        byte[] expected = {0x04, 0x03, 0x02, 0x01};
        
        byte[] result = GostUtils.reverseBytes(input);
        
        assertArrayEquals(expected, result, "El arreglo de bytes no se invirtió correctamente.");
    }

    @Test
    public void testFromLittleEndianBytes() {
        // Hash en Little-Endian: [0x05, 0x00]
        byte[] littleEndianInput = {0x05, 0x00};
        
        // En formato Big-Endian debería ser [0x00, 0x05], es decir, el número 5.
        BigInteger expected = new BigInteger("5");
        BigInteger result = GostUtils.fromLittleEndianBytes(littleEndianInput);
        
        assertEquals(expected, result, "Fallo al convertir Little-Endian a BigInteger.");
    }
    

    @Test
    public void testToLittleEndianBytes_ManejoDelSigno() {
        // En criptografía, el número 255 (0xFF) suele causar problemas porque en Java
        // el bit más significativo en 1 indica un número negativo. BigInteger suele añadir un 
        // byte [0x00] extra al inicio para indicar que es positivo: [0x00, 0xFF].
        BigInteger number = new BigInteger("255"); 
        
        // Queremos exportar el número 255 (0xFF) a un arreglo Little-Endian de 4 bytes.
        // Esperamos: [0xFF, 0x00, 0x00, 0x00] y SIN el byte de signo extra.
        byte[] expected = {(byte) 0xFF, 0x00, 0x00, 0x00};
        
        byte[] result = GostUtils.toLittleEndianBytes(number, 4);
        
        assertArrayEquals(expected, result, "Fallo al exportar BigInteger a Little-Endian o al manejar el byte de signo.");
    }
}
