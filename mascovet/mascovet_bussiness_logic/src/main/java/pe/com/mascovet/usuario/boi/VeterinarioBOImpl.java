package pe.com.mascovet.usuario.boi;

import pe.com.mascovet.cita.dao.HorarioAtencionDAO;
import pe.com.mascovet.cita.impl.HorarioAtencionImpl;
import pe.com.mascovet.cita.model.HorarioAtencion;
import pe.com.mascovet.config.TransactionContext;
import pe.com.mascovet.usuario.bo.IVeterinarioBO;
import pe.com.mascovet.usuario.dao.VeterinarioDAO;
import pe.com.mascovet.usuario.impl.VeterinarioImpl;
import pe.com.mascovet.usuario.model.Veterinario;
import pe.com.mascovet.usuario.seguridad.ContrasenaHash;

import java.util.List;

public class VeterinarioBOImpl implements IVeterinarioBO {

    private VeterinarioDAO daoVeterinario;
    private HorarioAtencionDAO daoHorarioAtencion;

    public VeterinarioBOImpl() {
        daoVeterinario = new VeterinarioImpl();
        daoHorarioAtencion = new HorarioAtencionImpl();
    }

    private void validarVeterinario(Veterinario veterinario) {
        if (veterinario == null)
            throw new RuntimeException("El veterinario que se quiere registrar es null");
        if (veterinario.getDni() == null || veterinario.getDni().trim().isEmpty())
            throw new RuntimeException("El DNI del veterinario es obligatorio");
        if (veterinario.getDni().length() > 9)
            throw new RuntimeException("El DNI no debe exceder los 9 caracteres");
        if (veterinario.getNombre() == null || veterinario.getNombre().trim().isEmpty())
            throw new RuntimeException("El nombre del veterinario es obligatorio");
        if (veterinario.getNombre().length() > 50)
            throw new RuntimeException("El nombre no debe exceder los 50 caracteres");
        if (veterinario.getApellidoPaterno() == null || veterinario.getApellidoPaterno().trim().isEmpty())
            throw new RuntimeException("El apellido del veterinario es obligatorio");
        if (veterinario.getApellidoPaterno().length() > 50)
            throw new RuntimeException("El apellido no debe exceder los 50 caracteres");
        if (veterinario.getApellidoMaterno() == null || veterinario.getApellidoMaterno().trim().isEmpty())
            throw new RuntimeException("El apellido materno es obligatorio");
        if (veterinario.getApellidoMaterno().length() > 50)
            throw new RuntimeException("El apellido materno no debe exceder los 50 caracteres");
        if (veterinario.getNombreUsuario() == null || veterinario.getNombreUsuario().trim().isEmpty())
            throw new RuntimeException("El nombre de usuario es obligatorio");
        if (veterinario.getNombreUsuario().length() > 100)
            throw new RuntimeException("El nombre de usuario no debe exceder los 100 caracteres");
        if (veterinario.getContrasena() == null || veterinario.getContrasena().trim().isEmpty())
            throw new RuntimeException("La contraseña es obligatoria");
        if (veterinario.getContrasena().length() > 255)
            throw new RuntimeException("La contraseña no debe exceder los 255 caracteres");
        if (veterinario.getNumeroColegiatura() == null || veterinario.getNumeroColegiatura().trim().isEmpty())
            throw new RuntimeException("El número de colegiatura es obligatorio");
        if (veterinario.getNumeroColegiatura().length() > 10)
            throw new RuntimeException("El número de colegiatura no debe exceder los 10 caracteres");
        if (veterinario.getEspecialidad() == null)
            throw new RuntimeException("La especialidad es obligatoria");
        if (veterinario.getCorreo() == null || veterinario.getCorreo().trim().isEmpty())
            throw new RuntimeException("El correo es obligatorio");
        if (veterinario.getCorreo().length() > 200)
            throw new RuntimeException("El correo no debe exceder los 200 caracteres");
        if (veterinario.getTelefono() == null || veterinario.getTelefono().trim().isEmpty())
            throw new RuntimeException("El teléfono es obligatorio");
        if (veterinario.getTelefono().length() > 20)
            throw new RuntimeException("El teléfono no debe exceder los 20 caracteres");
    }

    private void validarHorarioAtencion(HorarioAtencion horario) {
        if (horario == null)
            throw new RuntimeException("El horario de atención es null");
        if (horario.getDia() == null)
            throw new RuntimeException("El día del horario es obligatorio");
        if (horario.getHoraInicio() == null || horario.getHoraFin() == null)
            throw new RuntimeException("Las horas del horario son obligatorias");
        if (!horario.getHoraInicio().isBefore(horario.getHoraFin()))
            throw new RuntimeException("La hora de inicio debe ser anterior a la hora de fin");
    }

    @Override
    public int insertar(Veterinario veterinario) {
        validarVeterinario(veterinario);
        veterinario.setActivo(true);

        String contrasenaOriginal = veterinario.getContrasena();
        veterinario.setContrasena(ContrasenaHash.generar(contrasenaOriginal));

        try {
            return daoVeterinario.insertar(veterinario);
        } finally {
            veterinario.setContrasena(contrasenaOriginal);
        }
    }

    @Override
    public int modificar(Veterinario veterinario) {
        validarVeterinario(veterinario);
        if (veterinario.getIdUsuario() <= 0)
            throw new RuntimeException("El identificador del veterinario no es válido");
        return daoVeterinario.modificar(veterinario);
    }

    @Override
    public int eliminar(int idVeterinario) {
        if (idVeterinario <= 0)
            throw new RuntimeException("El identificador del veterinario no es válido");
        return daoVeterinario.eliminar(idVeterinario);
    }

    @Override
    public List<Veterinario> listarTodos() {
        return daoVeterinario.listarTodos();
    }

    @Override
    public Veterinario obtenerPorId(int idVeterinario) {
        if (idVeterinario <= 0)
            throw new RuntimeException("El identificador del veterinario no es válido");
        return daoVeterinario.obtenerPorId(idVeterinario);
    }

    @Override
    public int insertarConHorarios(Veterinario veterinario) {
        validarVeterinario(veterinario);
        veterinario.setActivo(true);

        if (veterinario.getHorariosAtencion() == null || veterinario.getHorariosAtencion().isEmpty())
            throw new RuntimeException("El veterinario debe tener al menos un horario de atención");
        for (HorarioAtencion horario : veterinario.getHorariosAtencion())
            validarHorarioAtencion(horario);

        List<HorarioAtencion> horarios = veterinario.getHorariosAtencion();
        for (int i = 0; i < horarios.size(); i++) {
            for (int j = i + 1; j < horarios.size(); j++) {
                HorarioAtencion primero = horarios.get(i);
                HorarioAtencion segundo = horarios.get(j);
                if (primero.getDia() == segundo.getDia()
                        && primero.getHoraInicio().isBefore(segundo.getHoraFin())
                        && primero.getHoraFin().isAfter(segundo.getHoraInicio()))
                    throw new RuntimeException("Los horarios del veterinario no pueden superponerse");
            }
        }

        try {
            TransactionContext.getConnection();
            daoVeterinario.insertar(veterinario);
            for (HorarioAtencion horario : veterinario.getHorariosAtencion()) {
                horario.setVeterinario(veterinario);
                daoHorarioAtencion.insertar(horario);
            }
            TransactionContext.commit();
            return 1;
        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new RuntimeException("Error:" + ex.getMessage());
        } finally {
            TransactionContext.close();
        }
    }
}
