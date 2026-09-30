package pe.com.mascovet.usuario.bo;

import pe.com.mascovet.config.TransactionContext;
import pe.com.mascovet.usuario.boi.IUsuarioBO;
import pe.com.mascovet.usuario.dao.UsuarioDAO;
import pe.com.mascovet.usuario.impl.UsuarioImpl;
import pe.com.mascovet.usuario.model.Usuario;

import java.util.List;

public class UsuarioBOImpl implements IUsuarioBO {

    private UsuarioDAO daoUsuario;

    public UsuarioBOImpl() {
        daoUsuario = new UsuarioImpl();
    }

    private void validarUsuario(Usuario usuario) {
        if (usuario == null)
            throw new RuntimeException("El usuario que se quiere registrar es null");
        if (usuario.getDni() == null || usuario.getDni().trim().isEmpty())
            throw new RuntimeException("El DNI del usuario es obligatorio");
        if (usuario.getDni().length() > 9)
            throw new RuntimeException("El DNI no debe exceder los 9 caracteres");
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty())
            throw new RuntimeException("El nombre del usuario es obligatorio");
        if (usuario.getNombre().length() > 150)
            throw new RuntimeException("El nombre no debe exceder los 150 caracteres");
        if (usuario.getApellido() == null || usuario.getApellido().trim().isEmpty())
            throw new RuntimeException("El apellido del usuario es obligatorio");
        if (usuario.getApellido().length() > 150)
            throw new RuntimeException("El apellido no debe exceder los 150 caracteres");
        if (usuario.getNombreUsuario() == null || usuario.getNombreUsuario().trim().isEmpty())
            throw new RuntimeException("El nombre de usuario es obligatorio");
        if (usuario.getNombreUsuario().length() > 100)
            throw new RuntimeException("El nombre de usuario no debe exceder los 100 caracteres");
        if (usuario.getContrasena() == null || usuario.getContrasena().trim().isEmpty())
            throw new RuntimeException("La contraseña es obligatoria");
        if (usuario.getContrasena().length() > 255)
            throw new RuntimeException("La contraseña no debe exceder los 255 caracteres");
    }

    @Override
    public int insertar(Usuario usuario) {
        validarUsuario(usuario);
        usuario.setActivo(true);
        try {
            int resultado = daoUsuario.insertar(usuario);
            TransactionContext.commit();
            return resultado;
        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new RuntimeException("Error:" + ex.getMessage());
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public int modificar(Usuario usuario) {
        validarUsuario(usuario);
        if (usuario.getIdUsuario() <= 0)
            throw new RuntimeException("El identificador del usuario no es válido");
        try {
            int resultado = daoUsuario.modificar(usuario);
            TransactionContext.commit();
            return resultado;
        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new RuntimeException("Error:" + ex.getMessage());
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public int eliminar(int idUsuario) {
        if (idUsuario <= 0)
            throw new RuntimeException("El identificador del usuario no es válido");
        return daoUsuario.eliminar(idUsuario);
    }

    @Override
    public List<Usuario> listarTodos() {
        return daoUsuario.listarTodos();
    }

    @Override
    public Usuario obtenerPorId(int idUsuario) {
        if (idUsuario <= 0)
            throw new RuntimeException("El identificador del usuario no es válido");
        return daoUsuario.obtenerPorId(idUsuario);
    }
}
