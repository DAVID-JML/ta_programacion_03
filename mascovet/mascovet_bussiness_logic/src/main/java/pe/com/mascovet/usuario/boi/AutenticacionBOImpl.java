package pe.com.mascovet.usuario.boi;

import pe.com.mascovet.usuario.bo.IAutenticacionBO;
import pe.com.mascovet.usuario.dao.AutenticacionDAO;
import pe.com.mascovet.usuario.impl.AutenticacionImpl;
import pe.com.mascovet.usuario.seguridad.ContrasenaHash;

import java.util.Locale;

public class AutenticacionBOImpl implements IAutenticacionBO {

    private static final int MAX_CORREO = 200, MAX_USUARIO = 100, MAX_CONTRASENA = 255;

    private static final String HASH_FALSO = ContrasenaHash.generar("contrasena-falsa");

    private final AutenticacionDAO dao = new AutenticacionImpl();

    @Override
    public Sesion autenticarCliente(String correo, String contrasena) {
        String correoLimpio = limpiar(correo, MAX_CORREO);
        if (correoLimpio == null || !contrasenaValida(contrasena)) return null;
        return verificar(dao.buscarClienteParaLogin(correoLimpio.toLowerCase(Locale.ROOT)), contrasena);
    }

    @Override
    public Sesion autenticarPersonal(String nombreUsuario, String contrasena) {
        String usuarioLimpio = limpiar(nombreUsuario, MAX_USUARIO);
        if (usuarioLimpio == null || !contrasenaValida(contrasena)) return null;
        return verificar(dao.buscarPersonalParaLogin(usuarioLimpio), contrasena);
    }

    @Override
    public boolean cambiarContrasena(int idUsuario, String actual, String nueva) {
        if (idUsuario <= 0 || !contrasenaValida(actual) || !contrasenaValida(nueva)
                || nueva.length() < 8 || nueva.equals(actual)) return false;

        String hash = dao.obtenerHashPorId(idUsuario);
        boolean coincide = ContrasenaHash.verificar(actual, hash != null ? hash : HASH_FALSO);
        if (hash == null || !coincide) return false;

        return dao.actualizarHash(idUsuario, ContrasenaHash.generar(nueva));
    }
    // La lógica común, escrita una sola vez
    private Sesion verificar(AutenticacionDAO.DatosLogin datos, String contrasena) {
        String hash = datos != null ? datos.hashContrasena() : HASH_FALSO;
        boolean coincide = ContrasenaHash.verificar(contrasena, hash);
        if (datos == null || !coincide) return null;
        return new Sesion(datos.idUsuario(), datos.nombre(), datos.nombreUsuario(), datos.rol());
    }

    private static String limpiar(String valor, int max) {
        if (valor == null || valor.isBlank()) return null;
        String limpio = valor.trim();
        return limpio.length() > max ? null : limpio;
    }

    private static boolean contrasenaValida(String c) {
        return c != null && !c.isBlank() && c.length() <= MAX_CONTRASENA;
    }
}
