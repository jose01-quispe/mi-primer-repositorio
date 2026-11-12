package pe.edu.upeu.sysventas.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Encriptación de claves con PBKDF2-HMAC-SHA256 + salt aleatorio (solo JDK, sin librerías extra).
 * Formato guardado: iteraciones$salt$hash (Base64). La clave nunca se guarda en texto plano.
 */
public final class PasswordUtil {
    private static final int ITERACIONES = 65_536;
    private static final int LONGITUD_BITS = 256;
    private static final int LONGITUD_SALT = 16;

    private PasswordUtil() {
    }
//cri
    public static String encriptar(String clave) {
        byte[] salt = new byte[LONGITUD_SALT];
        new SecureRandom().nextBytes(salt);
        byte[] hash = derivar(clave, salt, ITERACIONES);
        return ITERACIONES + "$" + Base64.getEncoder().encodeToString(salt) + "$" + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verificar(String clave, String guardada) {
        if (clave == null || guardada == null) {
            return false;
        }
        String[] partes = guardada.split("\\$");
        if (partes.length != 3) {
            return false;
        }
        try {
            int iteraciones = Integer.parseInt(partes[0]);
            byte[] salt = Base64.getDecoder().decode(partes[1]);
            byte[] esperado = Base64.getDecoder().decode(partes[2]);
            return MessageDigest.isEqual(esperado, derivar(clave, salt, iteraciones));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
//real
    private static byte[] derivar(String clave, byte[] salt, int iteraciones) {
        try {
            PBEKeySpec spec = new PBEKeySpec(clave.toCharArray(), salt, iteraciones, LONGITUD_BITS);
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("No se pudo encriptar la clave", e);
        }
    }
}
