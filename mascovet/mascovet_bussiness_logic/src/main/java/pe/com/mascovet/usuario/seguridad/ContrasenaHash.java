package pe.com.mascovet.usuario.seguridad;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class ContrasenaHash {

    private static final String FORMATO = "pbkdf2-sha256-v1";
    private static final int ITERACIONES = 600_000;
    private static final int BYTES_SAL = 16;
    private static final int BITS_HASH = 256;
    private static final SecureRandom ALEATORIO = new SecureRandom();

    private ContrasenaHash() {
    }

    public static String generar(String contrasena) {
        byte[] sal = new byte[BYTES_SAL];
        ALEATORIO.nextBytes(sal);

        byte[] hash = derivar(contrasena, sal);
        Base64.Encoder encoder = Base64.getEncoder();

        return FORMATO + "$" + encoder.encodeToString(sal)
                + "$" + encoder.encodeToString(hash);
    }

    public static boolean verificar(String contrasena, String almacenado) {
        if (contrasena == null || almacenado == null) {
            return false;
        }

        String[] partes = almacenado.split("\\$", -1);
        if (partes.length != 3 || !FORMATO.equals(partes[0])) {
            return false;
        }

        byte[] sal;
        byte[] hashGuardado;

        try {
            Base64.Decoder decoder = Base64.getDecoder();
            sal = decoder.decode(partes[1]);
            hashGuardado = decoder.decode(partes[2]);
        } catch (IllegalArgumentException ex) {
            return false;
        }

        if (sal.length != BYTES_SAL || hashGuardado.length != BITS_HASH / 8) {
            return false;
        }

        byte[] hashIngresado = derivar(contrasena, sal);
        return MessageDigest.isEqual(hashGuardado, hashIngresado);
    }

    private static byte[] derivar(String contrasena, byte[] sal) {
        PBEKeySpec spec = new PBEKeySpec(
                contrasena.toCharArray(), sal, ITERACIONES, BITS_HASH
        );

        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec)
                    .getEncoded();
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("Error al procesar la contraseña", ex);
        } finally {
            spec.clearPassword();
        }
    }
}