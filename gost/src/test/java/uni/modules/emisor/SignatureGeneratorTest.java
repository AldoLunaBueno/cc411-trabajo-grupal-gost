package uni.modules.emisor;
import org.junit.jupiter.api.Test;

import uni.model.DomainParameters;
import uni.model.Point;
import uni.model.Signature;

import java.math.BigInteger;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SignatureGeneratorTest {

    // La instancia del módulo que desarrollará Jharvy
    private final SignatureGenerator signer = new GostSigner(); 

    @Test
    public void testSignWithAppendixA1Vectors() {
        // 1. Configuración de los parámetros del dominio (256 bits)
        BigInteger p = new BigInteger("8000000000000000000000000000000000000000000000000000000000000431", 16);
        BigInteger a = new BigInteger("7", 16);
        BigInteger b = new BigInteger("5FBFF498AA938CE739B8E022FBAFEF40563F6E6A3472FC2A514C0CE9DAE23B7E", 16);
        BigInteger q = new BigInteger("8000000000000000000000000000000150FE8A1892976154C59CFC193ACCF5B3", 16);
        Point P = new Point(
            new BigInteger("2", 16),
            new BigInteger("8E2A8A0E65147D4BD6316030E16D19C85C97F0A9CA267122B96ABBCEA7E8FC8", 16)
        );
        DomainParameters params = new DomainParameters(p, a, b, q, P);

        // 2. Entradas estáticas del Apéndice A.1
        // El hash del mensaje ya procesado (e)
        BigInteger e = new BigInteger("2DFBC1B372D89A1188C09C52E0EEC61FCE52032AB1022E8E67ECE6672B043EE5", 16);
        
        // La clave privada del emisor (d)
        BigInteger d = new BigInteger("7A929ADE789BB9BE10ED359DD39A72C11B60961F49397EEE1D19CE9891EC3B28", 16);
        
        // El número aleatorio efímero (k) especificado en el estándar para este ejemplo exacto
        BigInteger k = new BigInteger("77105C9B20BCD3122823C8CF6FCC7B956DE33814E95B7FE64FED924594DCEAB3", 16);
        // 3. Valores esperados de la firma
        BigInteger expectedR = new BigInteger("41AA28D2F1AB148280CD9ED56FEDA41974053554A42767B83AD043FD39DC0493", 16);
        BigInteger expectedS = new BigInteger("1456C64BA4642A1653C235A98A60249BCD6D3F746B631DF928014F6C5BF9C40", 16);

        // 4. Ejecución
        Signature firma = signer.sign(e, d, k, params);

        // 5. Validaciones
        assertEquals(expectedR, firma.r, "El valor R de la firma no coincide. Revisa la multiplicación escalar del punto C = kP.");
        assertEquals(expectedS, firma.s, "El valor S de la firma no coincide. Revisa la ecuación modular s = (rd + ke) mod q.");
    }
}