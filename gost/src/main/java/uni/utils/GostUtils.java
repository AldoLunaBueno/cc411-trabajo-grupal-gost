package uni.utils;

import java.math.BigInteger;
import java.util.HexFormat;

/**
 * Clase centralizada de utilidades para evitar errores de Endianness y conversiones
 * repetitivas en los módulos de Streebog, Emisor y Receptor.
 */
public class GostUtils {

    /**
     * Convertir arreglo de bytes Big-Endian a Little-Endian (y viceversa).
     * Retornar un nuevo arreglo para evitar efectos secundarios (inmutabilidad).
     */
    public static byte[] reverseBytes(byte[] input) {
        if (input == null) return null;
        byte[] reversed = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            reversed[i] = input[input.length - 1 - i];
        }
        return reversed;
    }

    /**
     * Crear un BigInteger desde bytes en Little-Endian
     * invertiendo los bytes y garantizando que el número sea positivo.
     */
    public static BigInteger fromLittleEndianBytes(byte[] littleEndianBytes) {
        byte[] bigEndianBytes = reverseBytes(littleEndianBytes);
        // El '1' asegura que el número se interprete como positivo (sin signo)
        return new BigInteger(1, bigEndianBytes);
    }

    /**
     * Extraer bytes Little-Endian desde un BigInteger.
     * Ejemplo de uso: exportar firma (r, s).
     */
    public static byte[] toLittleEndianBytes(BigInteger number, int byteLength) {
        byte[] bigEndianBytes = number.toByteArray();
        byte[] padded = new byte[byteLength];
        
        // Manejar el posible byte de signo que añade BigInteger
        int startIdx = (bigEndianBytes[0] == 0) ? 1 : 0;
        int lengthToCopy = bigEndianBytes.length - startIdx;
        
        System.arraycopy(bigEndianBytes, startIdx, padded, byteLength - lengthToCopy, lengthToCopy);
        
        return reverseBytes(padded);
    }

    /**
     * Convierte una cadena Hexadecimal a un arreglo de bytes.
     */
    public static byte[] fromHexString(String hexString) {
        return HexFormat.of().parseHex(hexString);
    }

    /**
     * Convierte un arreglo de bytes a una cadena Hexadecimal (útil para imprimir logs).
     */
    public static String toHexString(byte[] bytes) {
        return HexFormat.of().formatHex(bytes);
    }
}