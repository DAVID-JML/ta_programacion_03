package pe.com.mascovet.usuario.bo;

import pe.com.mascovet.bo.IBaseBO;
import pe.com.mascovet.usuario.model.Usuario;

public interface IUsuarioBO extends IBaseBO<Usuario> {
    SesionPersonal autenticarPersonal(String nombreUsuario, String contrasena);

    record SesionPersonal(
            int idUsuario,
            String nombre,
            String nombreUsuario,
            String rol
    ) {}
}
