package pe.com.mascovet.usuario.bo;

public interface IAutenticacionBO {
    record Sesion(int idUsuario, String nombre, String nombreUsuario, String rol) {}

    Sesion autenticarCliente(String correo, String contrasena);
    Sesion autenticarPersonal(String nombreUsuario, String contrasena);
}
