package pe.com.mascovet.cita.boi;

import pe.com.mascovet.cita.bo.IHorarioAtencionBO;
import pe.com.mascovet.cita.dao.HorarioAtencionDAO;
import pe.com.mascovet.cita.impl.HorarioAtencionImpl;
import pe.com.mascovet.cita.model.HorarioAtencion;

import java.util.List;

public class HorarioAtencionBOImpl implements IHorarioAtencionBO {

    private HorarioAtencionDAO daoHorarioAtencion;

    public HorarioAtencionBOImpl() {
        daoHorarioAtencion = new HorarioAtencionImpl();
    }

    private void validarHorarioAtencion(HorarioAtencion horarioAtencion) {
        if (horarioAtencion == null)
            throw new RuntimeException("El horario de atención es null");
        if (horarioAtencion.getVeterinario() == null || horarioAtencion.getVeterinario().getIdUsuario() <= 0)
            throw new RuntimeException("El veterinario asociado al horario no es válido");
        if (horarioAtencion.getDia() == null)
            throw new RuntimeException("El día del horario es obligatorio");
        if (horarioAtencion.getHoraInicio() == null || horarioAtencion.getHoraFin() == null)
            throw new RuntimeException("Las horas del horario son obligatorias");
        if (!horarioAtencion.getHoraInicio().isBefore(horarioAtencion.getHoraFin()))
            throw new RuntimeException("La hora de inicio debe ser anterior a la hora de fin");
    }

    private void validarSuperposicion(HorarioAtencion horario, int idExcluido) {
        for (HorarioAtencion existente : daoHorarioAtencion.listarTodos()) {
            if (existente.getIdHorario() == idExcluido)
                continue;
            if (existente.getVeterinario().getIdUsuario() == horario.getVeterinario().getIdUsuario()
                    && existente.getDia() == horario.getDia()
                    && horario.getHoraInicio().isBefore(existente.getHoraFin())
                    && horario.getHoraFin().isAfter(existente.getHoraInicio()))
                throw new RuntimeException("El veterinario ya tiene un horario superpuesto para ese día");
        }
    }

    @Override
    public int insertar(HorarioAtencion horarioAtencion) {
        validarHorarioAtencion(horarioAtencion);
        validarSuperposicion(horarioAtencion, 0);
        return daoHorarioAtencion.insertar(horarioAtencion);
    }

    @Override
    public int modificar(HorarioAtencion horarioAtencion) {
        validarHorarioAtencion(horarioAtencion);
        if (horarioAtencion.getIdHorario() <= 0)
            throw new RuntimeException("El identificador del horario no es válido");
        validarSuperposicion(horarioAtencion, horarioAtencion.getIdHorario());
        return daoHorarioAtencion.modificar(horarioAtencion);
    }

    @Override
    public int eliminar(int idHorario) {
        if (idHorario <= 0)
            throw new RuntimeException("El identificador del horario no es válido");
        return daoHorarioAtencion.eliminar(idHorario);
    }

    @Override
    public List<HorarioAtencion> listarTodos() {
        return daoHorarioAtencion.listarTodos();
    }

    @Override
    public HorarioAtencion obtenerPorId(int idHorario) {
        if (idHorario <= 0)
            throw new RuntimeException("El identificador del horario no es válido");
        return daoHorarioAtencion.obtenerPorId(idHorario);
    }
}
