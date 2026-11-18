package pe.com.mascovet.usuario.dao;

public interface AutenticacionDAO {
    record DatosLogin(
            int idUsuario,
            String nombre,
            String nombreUsuario,
            String hashContrasena,
            String rol
    ) {}

    /** Busca un cliente por correo. Devuelve null si no existe. */
    DatosLogin buscarClienteParaLogin(String correo);

    /** Busca un miembro del personal (veterinario, recepcionista o administrador) por nombre de usuario. Devuelve null si no existe o está deshabilitado. */
    DatosLogin buscarPersonalParaLogin(String nombreUsuario);
}
