package pe.com.mascovet.usuario.bo;

import pe.com.mascovet.bo.IBaseBO;
import pe.com.mascovet.usuario.model.CuentaUsuario;

public interface ICuentaUsuarioBO extends IBaseBO<CuentaUsuario> {
    boolean verificar(CuentaUsuario cuentaUsuario);
    CuentaUsuario autenticar(CuentaUsuario cuentaUsuario);
    int cambiarContrasena(CuentaUsuario cuentaUsuario);
}
