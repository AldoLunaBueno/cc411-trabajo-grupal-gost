package uni.modules.receptor;

import org.junit.jupiter.api.Test;

import uni.model.DomainParameters;
import uni.model.Point;
import uni.model.Signature;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.math.BigInteger;

public class SignatureVerifierTest {

    @Test
    public void testVerifyWithAppendixA1Vectors() {
        // 1. Configurar los parámetros de la curva del Apéndice A.1
        BigInteger p = new BigInteger("8000000000000000000000000000000000000000000000000000000000000431", 16);
        BigInteger a = new BigInteger("7", 16);
        BigInteger b = new BigInteger("5FBFF498AA938CE739B8E022FBAFEF40563F6E6A3472FC2A514C0CE9DAE23B7E", 16);
        BigInteger q = new BigInteger("8000000000000000000000000000000150FE8A1892976154C59CFC193ACCF5B3", 16);
        
        Point P = new Point(
            new BigInteger("2", 16),
            new BigInteger("8E2A8A0E65147D4BD6316030E16D19C85C97F0A9CA267122B96ABBCEA7E8FC8", 16)
        );
        DomainParameters params = new DomainParameters(p, a, b, q, P);

        // 2. Configurar la Clave Pública Q del Ejemplo 1
        Point Q = new Point(
            new BigInteger("7F2B49E270DB6D90D8595BEC458B50C58585BA1D4E9B788F6689DBD8E56FD80B", 16),
            new BigInteger("26F1B489D6701DD185C8413A977B3CBBAF64D1C593D26627DFFB101A87FF77DA1", 16)
        );

        // 3. Configurar el entero 'e' (hash) y la Firma generada (r, s) del Ejemplo 1
        BigInteger e = new BigInteger("2DFBC1B372D89A1188C09C52E0EEC61FCE52032AB1022E8E67ECE6672B043EE5", 16);
        BigInteger r = new BigInteger("41AA28D2F1AB148280CD9ED56FEDA41974053554A42767B83AD043FD39DC0493", 16);
        BigInteger s = new BigInteger("1456C64BA4642A1653C235A98A60249BCD6D3F746B631DF928014F6C5BF9C40", 16);
        Signature sig = new Signature(r, s);

        // 4. Instanciar la implementación
        SignatureVerifier verifier = new GostVerifier(); 

        // 5. Ejecutar y validar
        boolean isValid = verifier.verify(sig, e, Q, params);
        
        // Assert: La firma debe ser válida según el estándar oficial
        assertTrue(isValid, "La verificación falló. Revisa la reconstrucción del punto C y el inverso modular.");
    }

    @Test
    public void testVerifyWithWrongAppendixA1Vectors_OneBitAlteration() {
        // 1. Configurar los parámetros de la curva del Apéndice A.1
        BigInteger p = new BigInteger("8000000000000000000000000000000000000000000000000000000000000431", 16);
        BigInteger a = new BigInteger("7", 16);
        BigInteger b = new BigInteger("5FBFF498AA938CE739B8E022FBAFEF40563F6E6A3472FC2A514C0CE9DAE23B7E", 16);
        BigInteger q = new BigInteger("8000000000000000000000000000000150FE8A1892976154C59CFC193ACCF5B3", 16);
        
        Point P = new Point(
            new BigInteger("2", 16),
            new BigInteger("8E2A8A0E65147D4BD6316030E16D19C85C97F0A9CA267122B96ABBCEA7E8FC8", 16)
        );
        DomainParameters params = new DomainParameters(p, a, b, q, P);

        // 2. Configurar la Clave Pública Q del Ejemplo 1
        Point Q = new Point(
            new BigInteger("7F2B49E270DB6D90D8595BEC458B50C58585BA1D4E9B788F6689DBD8E56FD80B", 16),
            new BigInteger("26F1B489D6701DD185C8413A977B3CBBAF64D1C593D26627DFFB101A87FF77DA1", 16)
        );

        // 3. Configurar el entero 'e' (hash) y la Firma generada (r, s) del Ejemplo 1
        BigInteger e = new BigInteger("2DFBC1B372D89A1188C09C52E0EEC61FCE52032AB1022E8E67ECE6672B043EE5", 16);

        // el valor 'r' está alterado en un único bit (cambiamos el bit menos significativo para este ejemplo)
        BigInteger r = new BigInteger("41AA28D2F1AB148280CD9ED56FEDA41974053554A42767B83AD043FD39DC0494", 16);
        
        BigInteger s = new BigInteger("1456C64BA4642A1653C235A98A60249BCD6D3F746B631DF928014F6C5BF9C40", 16);
        Signature sig = new Signature(r, s);

        // 4. Instanciar la implementación de Melissa (aquí usas la clase que ella desarrollará)
        SignatureVerifier verifier = new GostVerifier(); 

        // 5. Ejecutar y validar
        boolean isValid = verifier.verify(sig, e, Q, params);
        
        // Assert: La firma debe ser INVÁLIDA según el estándar oficial
        assertFalse(isValid, "La verificación falló. Revisa la reconstrucción del punto C y el inverso modular.");
    }
}