package pe.com.mascovet.usuario.dao;

import pe.com.mascovet.dao.IDAO;
import pe.com.mascovet.usuario.model.CuentaUsuario;

public interface CuentaUsuarioDAO extends IDAO<CuentaUsuario> {
    boolean verificar(CuentaUsuario cuentaUsuario);
    CuentaUsuario autenticar(CuentaUsuario cuentaUsuario);
    int cambiarContrasena(CuentaUsuario cuentaUsuario);
}
