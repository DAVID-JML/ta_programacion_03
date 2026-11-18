package pe.com.mascovet.usuario.boi;

import pe.com.mascovet.usuario.bo.ICuentaUsuarioBO;
import pe.com.mascovet.usuario.dao.CuentaUsuarioDAO;
import pe.com.mascovet.usuario.impl.CuentaUsuarioImpl;
import pe.com.mascovet.usuario.model.CuentaUsuario;
import java.util.List;

public class CuentaUsuarioBOImpl implements ICuentaUsuarioBO {
    private CuentaUsuarioDAO daoCuentaUsuario;

    public CuentaUsuarioBOImpl() { daoCuentaUsuario = new CuentaUsuarioImpl(); }

    @Override
    public boolean verificar(CuentaUsuario cuentaUsuario) {
        return daoCuentaUsuario.verificar(cuentaUsuario);
    }
    @Override
    public CuentaUsuario autenticar(CuentaUsuario cuentaUsuario) {
        return daoCuentaUsuario.autenticar(cuentaUsuario);
    }
    @Override
    public int cambiarContrasena(CuentaUsuario cuentaUsuario) {
        if (cuentaUsuario == null || cuentaUsuario.getIdUsuario() <= 0 ||
            cuentaUsuario.getPassword() == null || cuentaUsuario.getPassword().isEmpty() ||
            cuentaUsuario.getNuevaPassword() == null || cuentaUsuario.getNuevaPassword().length() < 8 ||
            cuentaUsuario.getPassword().equals(cuentaUsuario.getNuevaPassword())) {
            throw new IllegalArgumentException("Contraseña actual y nueva contraseña válida son obligatorias");
        }
        return daoCuentaUsuario.cambiarContrasena(cuentaUsuario);
    }
    @Override
    public int insertar(CuentaUsuario cuentaUsuario) {
        validar(cuentaUsuario, true);
        return daoCuentaUsuario.insertar(cuentaUsuario);
    }
    @Override
    public int modificar(CuentaUsuario cuentaUsuario) {
        validar(cuentaUsuario, false);
        if (cuentaUsuario.getIdCuentaUsuario() <= 0)
            throw new RuntimeException("Id de cuenta inválido");
        return daoCuentaUsuario.modificar(cuentaUsuario);
    }
    private void validar(CuentaUsuario cuenta, boolean nueva) {
        if (cuenta == null || cuenta.getUsername() == null || cuenta.getUsername().isBlank())
            throw new RuntimeException("Nombre de cuenta obligatorio");
        if (cuenta.getUsername().length() > 100)
            throw new RuntimeException("Nombre de cuenta demasiado largo");
        if (nueva && (cuenta.getIdUsuario() <= 0 || cuenta.getPassword() == null || cuenta.getPassword().length() < 8))
            throw new RuntimeException("Usuario asociado y contraseña de 8 caracteres son obligatorios");
        if (cuenta.getPassword() != null && !cuenta.getPassword().isEmpty() && cuenta.getPassword().length() < 8)
            throw new RuntimeException("La nueva contraseña debe tener al menos 8 caracteres");
    }
    @Override
    public int eliminar(int idCuentaUsuario) { return daoCuentaUsuario.eliminar(idCuentaUsuario); }
    @Override
    public List<CuentaUsuario> listarTodos() { return daoCuentaUsuario.listarTodos(); }
    @Override
    public CuentaUsuario obtenerPorId(int idCuentaUsuario) { return daoCuentaUsuario.obtenerPorId(idCuentaUsuario); }
}
