package pe.com.mascovet.usuario.dao;

import pe.com.mascovet.dao.IDAO;
import pe.com.mascovet.usuario.model.Usuario;

public interface UsuarioDAO extends IDAO<Usuario> {
    DatosLoginPersonal buscarPersonalParaLogin(String nombreUsuario);

    record DatosLoginPersonal(
            int idUsuario,
            String nombre,
            String nombreUsuario,
            String hashContrasena,
            String rol
    ) {}
}
