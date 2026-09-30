package pe.com.mascovet.usuario.boi;

import pe.com.mascovet.config.TransactionContext;
import pe.com.mascovet.usuario.bo.IAdministradorBO;
import pe.com.mascovet.usuario.dao.AdministradorDAO;
import pe.com.mascovet.usuario.impl.AdministradorImpl;
import pe.com.mascovet.usuario.model.Administrador;

import java.util.List;

public class AdministradorBOImpl implements IAdministradorBO {

    private AdministradorDAO daoAdministrador;

    public AdministradorBOImpl() {
        daoAdministrador = new AdministradorImpl();
    }

    private void validarAdministrador(Administrador administrador) {
        if (administrador == null)
            throw new RuntimeException("El administrador que se quiere registrar es null");
        if (administrador.getDni() == null || administrador.getDni().trim().isEmpty())
            throw new RuntimeException("El DNI del administrador es obligatorio");
        if (administrador.getDni().length() > 9)
            throw new RuntimeException("El DNI no debe exceder los 9 caracteres");
        if (administrador.getNombre() == null || administrador.getNombre().trim().isEmpty())
            throw new RuntimeException("El nombre del administrador es obligatorio");
        if (administrador.getNombre().length() > 150)
            throw new RuntimeException("El nombre no debe exceder los 150 caracteres");
        if (administrador.getApellido() == null || administrador.getApellido().trim().isEmpty())
            throw new RuntimeException("El apellido del administrador es obligatorio");
        if (administrador.getApellido().length() > 150)
            throw new RuntimeException("El apellido no debe exceder los 150 caracteres");
        if (administrador.getNombreUsuario() == null || administrador.getNombreUsuario().trim().isEmpty())
            throw new RuntimeException("El nombre de usuario es obligatorio");
        if (administrador.getNombreUsuario().length() > 100)
            throw new RuntimeException("El nombre de usuario no debe exceder los 100 caracteres");
        if (administrador.getContrasena() == null || administrador.getContrasena().trim().isEmpty())
            throw new RuntimeException("La contraseña es obligatoria");
        if (administrador.getContrasena().length() > 255)
            throw new RuntimeException("La contraseña no debe exceder los 255 caracteres");
        if (administrador.getCargo() == null || administrador.getCargo().trim().isEmpty())
            throw new RuntimeException("El cargo del administrador es obligatorio");
        if (administrador.getCargo().length() > 100)
            throw new RuntimeException("El cargo no debe exceder los 100 caracteres");
    }

    @Override
    public int insertar(Administrador administrador) {
        validarAdministrador(administrador);
        administrador.setActivo(true);
        try {
            int resultado = daoAdministrador.insertar(administrador);
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
    public int modificar(Administrador administrador) {
        validarAdministrador(administrador);
        if (administrador.getIdUsuario() <= 0)
            throw new RuntimeException("El identificador del administrador no es válido");
        try {
            int resultado = daoAdministrador.modificar(administrador);
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
    public int eliminar(int idAdministrador) {
        if (idAdministrador <= 0)
            throw new RuntimeException("El identificador del administrador no es válido");
        return daoAdministrador.eliminar(idAdministrador);
    }

    @Override
    public List<Administrador> listarTodos() {
        return daoAdministrador.listarTodos();
    }

    @Override
    public Administrador obtenerPorId(int idAdministrador) {
        if (idAdministrador <= 0)
            throw new RuntimeException("El identificador del administrador no es válido");
        return daoAdministrador.obtenerPorId(idAdministrador);
    }
}
