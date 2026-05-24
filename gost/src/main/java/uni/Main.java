package uni;

import java.math.BigInteger;

import uni.model.DomainParameters;
import uni.model.Point;
import uni.model.Signature;
import uni.modules.emisor.GostSigner;
import uni.modules.emisor.SignatureGenerator;
import uni.modules.hash.HashService;
import uni.modules.hash.Streebog;
import uni.modules.receptor.GostVerifier;
import uni.modules.receptor.SignatureVerifier;

public class Main {

    public static void main(String[] args) {
        System.out.println("Integración de los estándares GOST R 34.10-2012 (Curvas Elípticas) y GOST R 34.11-2012 (función hash Streebog)\n");

        // 1. Inicialización de datos
        DomainParameters params = configurarParametrosCurva();
        BigInteger clavePrivada = obtenerClavePrivada();
        Point clavePublica = obtenerClavePublica();

        // 2. Instanciación de los módulos (Inyección de dependencias manual)
        HashService hashService = new Streebog();           // Benja
        SignatureGenerator signer = new GostSigner();       // Jharvy
        SignatureVerifier verifier = new GostVerifier();    // Melissa

        // 3. Ejecución
        String mensajeTexto = "Trabajo Final de Seguridad en Sistemas Informáticos - UNI";
        ejecutarProtocoloGost(mensajeTexto, params, clavePrivada, clavePublica, hashService, signer, verifier);
    }

    /**
     * Encapsula toda la configuración matemática del Apéndice A.1 (256 bits).
     */
    private static DomainParameters configurarParametrosCurva() {
        BigInteger p = new BigInteger("8000000000000000000000000000000000000000000000000000000000000431", 16);
        BigInteger a = new BigInteger("7", 16);
        BigInteger b = new BigInteger("5FBFF498AA938CE739B8E022FBAFEF40563F6E6A3472FC2A514C0CE9DAE23B7E", 16);
        BigInteger q = new BigInteger("8000000000000000000000000000000150FE8A1892976154C59CFC193ACCF5B3", 16);
        Point P = new Point(
            new BigInteger("2", 16),
            new BigInteger("8E2A8A0E65147D4BD6316030E16D19C85C97F0A9CA267122B96ABBCEA7E8FC8", 16)
        );
        return new DomainParameters(p, a, b, q, P);
    }

    /**
     * Retorna la Clave Privada (d) del emisor.
     */
    private static BigInteger obtenerClavePrivada() {
        return new BigInteger("7A929ADE789BB9BE10ED359DD39A72C11B60961F49397EEE1D19CE9891EC3B28", 16);
    }

    /**
     * Retorna la Clave Pública (Q) del emisor, donde Q = d * P.
     */
    private static Point obtenerClavePublica() {
        return new Point(
            new BigInteger("7F2B49E270DB6D90D8595BEC458B50C58585BA1D4E9B788F6689DBD8E56FD80B", 16),
            new BigInteger("26F1B489D6701DD185C8413A977B3CBBAF64D1C593D26627DFFB101A87FF77DA1", 16)
        );
    }

    /**
     * Orquesta el flujo completo del estándar utilizando los módulos del equipo.
     */
    private static void ejecutarProtocoloGost(String mensajeTexto, 
                                              DomainParameters params, 
                                              BigInteger clavePrivada, 
                                              Point clavePublica, 
                                              HashService hashService, 
                                              SignatureGenerator signer, 
                                              SignatureVerifier verifier) {
        
        byte[] mensajeBytes = mensajeTexto.getBytes();
        System.out.println("[*] Iniciando proceso para el mensaje: '" + mensajeTexto + "'\n");

        try {
            // PASO 1: Calcular Hash (Benja)
            System.out.println("[Paso 1] Procesando Hash (Módulo Streebog)...");
            BigInteger e = hashService.computeHashInteger(mensajeBytes, params.q);
            System.out.println("   Valor 'e' calculado: " + e.toString(16).toUpperCase() + "\n");

            // PASO 2: Generar Firma (Jharvy)
            System.out.println("[Paso 2] Generando Firma Digital (Emisor)...");
            Signature firma = signer.sign(e, clavePrivada, params);
            System.out.println("   Firma R: " + firma.r.toString(16).toUpperCase());
            System.out.println("   Firma S: " + firma.s.toString(16).toUpperCase() + "\n");

            // PASO 3: Verificar Firma (Melissa)
            System.out.println("[Paso 3] Verificando Firma Digital (Receptor)...");
            boolean esValida = verifier.verify(firma, e, clavePublica, params);

            // RESULTADO
            mostrarResultado(esValida);

        } catch (Exception ex) {
            System.err.println("\n[!] Error durante la ejecución del protocolo: " + ex.getMessage());
        }
    }

    /**
     * Imprime el veredicto final en consola.
     */
    private static void mostrarResultado(boolean esValida) {
        if (esValida) {
            System.out.println("¡ÉXITO! La firma es VÁLIDA y auténtica.");
        } else {
            System.out.println("¡ALERTA! La firma es INVÁLIDA o el mensaje fue alterado.");
        }
    }
}