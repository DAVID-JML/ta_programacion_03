package pe.com.mascovet.usuario.boi;

import pe.com.mascovet.config.TransactionContext;
import pe.com.mascovet.usuario.bo.IRecepcionistaBO;
import pe.com.mascovet.usuario.dao.RecepcionistaDAO;
import pe.com.mascovet.usuario.impl.RecepcionistaImpl;
import pe.com.mascovet.usuario.model.Recepcionista;

import java.util.List;

public class RecepcionistaBOImpl implements IRecepcionistaBO {

    private RecepcionistaDAO daoRecepcionista;

    public RecepcionistaBOImpl() {
        daoRecepcionista = new RecepcionistaImpl();
    }

    private void validarRecepcionista(Recepcionista recepcionista) {
        if (recepcionista == null)
            throw new RuntimeException("El recepcionista que se quiere registrar es null");
        if (recepcionista.getDni() == null || recepcionista.getDni().trim().isEmpty())
            throw new RuntimeException("El DNI del recepcionista es obligatorio");
        if (recepcionista.getDni().length() > 9)
            throw new RuntimeException("El DNI no debe exceder los 9 caracteres");
        if (recepcionista.getNombre() == null || recepcionista.getNombre().trim().isEmpty())
            throw new RuntimeException("El nombre del recepcionista es obligatorio");
        if (recepcionista.getNombre().length() > 150)
            throw new RuntimeException("El nombre no debe exceder los 150 caracteres");
        if (recepcionista.getApellido() == null || recepcionista.getApellido().trim().isEmpty())
            throw new RuntimeException("El apellido del recepcionista es obligatorio");
        if (recepcionista.getApellido().length() > 150)
            throw new RuntimeException("El apellido no debe exceder los 150 caracteres");
        if (recepcionista.getNombreUsuario() == null || recepcionista.getNombreUsuario().trim().isEmpty())
            throw new RuntimeException("El nombre de usuario es obligatorio");
        if (recepcionista.getNombreUsuario().length() > 100)
            throw new RuntimeException("El nombre de usuario no debe exceder los 100 caracteres");
        if (recepcionista.getContrasena() == null || recepcionista.getContrasena().trim().isEmpty())
            throw new RuntimeException("La contraseña es obligatoria");
        if (recepcionista.getContrasena().length() > 255)
            throw new RuntimeException("La contraseña no debe exceder los 255 caracteres");
        if (recepcionista.getTurno() == null || recepcionista.getTurno().trim().isEmpty())
            throw new RuntimeException("El turno del recepcionista es obligatorio");
        if (recepcionista.getTurno().length() > 150)
            throw new RuntimeException("El turno no debe exceder los 150 caracteres");
    }

    @Override
    public int insertar(Recepcionista recepcionista) {
        validarRecepcionista(recepcionista);
        recepcionista.setActivo(true);


        try {
            int resultado = daoRecepcionista.insertar(recepcionista);
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
    public int modificar(Recepcionista recepcionista) {
        validarRecepcionista(recepcionista);
        if (recepcionista.getIdUsuario() <= 0)
            throw new RuntimeException("El identificador del recepcionista no es válido");


        try {
            int resultado = daoRecepcionista.modificar(recepcionista);
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
    public int eliminar(int idRecepcionista) {
        if (idRecepcionista <= 0)
            throw new RuntimeException("El identificador del recepcionista no es válido");
        return daoRecepcionista.eliminar(idRecepcionista);
    }

    @Override
    public List<Recepcionista> listarTodos() {
        return daoRecepcionista.listarTodos();
    }

    @Override
    public Recepcionista obtenerPorId(int idRecepcionista) {
        if (idRecepcionista <= 0)
            throw new RuntimeException("El identificador del recepcionista no es válido");
        return daoRecepcionista.obtenerPorId(idRecepcionista);
    }
}
