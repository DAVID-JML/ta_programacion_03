package pe.com.mascovet.usuario.boi;

import pe.com.mascovet.usuario.bo.IRecepcionistaBO;
import pe.com.mascovet.usuario.dao.RecepcionistaDAO;
import pe.com.mascovet.usuario.impl.RecepcionistaImpl;
import pe.com.mascovet.usuario.model.Recepcionista;
import pe.com.mascovet.usuario.seguridad.ContrasenaHash;

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
        if (recepcionista.getNombre().length() > 50)
            throw new RuntimeException("El nombre no debe exceder los 50 caracteres");
        if (recepcionista.getApellidoPaterno() == null || recepcionista.getApellidoPaterno().trim().isEmpty())
            throw new RuntimeException("El apellido del recepcionista es obligatorio");
        if (recepcionista.getApellidoPaterno().length() > 50)
            throw new RuntimeException("El apellido no debe exceder los 50 caracteres");
        if (recepcionista.getApellidoMaterno() == null || recepcionista.getApellidoMaterno().trim().isEmpty())
            throw new RuntimeException("El apellido materno es obligatorio");
        if (recepcionista.getApellidoMaterno().length() > 50)
            throw new RuntimeException("El apellido materno no debe exceder los 50 caracteres");
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
        if (recepcionista.getCorreo() == null || recepcionista.getCorreo().trim().isEmpty())
            throw new RuntimeException("El correo es obligatorio");
        if (recepcionista.getCorreo().length() > 200)
            throw new RuntimeException("El correo no debe exceder los 200 caracteres");
        if (recepcionista.getTelefono() == null || recepcionista.getTelefono().trim().isEmpty())
            throw new RuntimeException("El teléfono es obligatorio");
        if (recepcionista.getTelefono().length() > 20)
            throw new RuntimeException("El teléfono no debe exceder los 20 caracteres");
    }

    @Override
    public int insertar(Recepcionista recepcionista) {
        validarRecepcionista(recepcionista);
        recepcionista.setActivo(true);

        String contrasenaOriginal = recepcionista.getContrasena();
        recepcionista.setContrasena(ContrasenaHash.generar(contrasenaOriginal));

        try {
            return daoRecepcionista.insertar(recepcionista);
        } finally {
            recepcionista.setContrasena(contrasenaOriginal);
        }
    }

    @Override
    public int modificar(Recepcionista recepcionista) {
        validarRecepcionista(recepcionista);
        if (recepcionista.getIdUsuario() <= 0)
            throw new RuntimeException("El identificador del recepcionista no es válido");
        return daoRecepcionista.modificar(recepcionista);
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
