package pe.com.mascovet.cita.boi;

import pe.com.mascovet.config.TransactionContext;
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

    private void validarSuperposicion(
            HorarioAtencion nuevo, int idHorarioExcluir) {

        List<HorarioAtencion> horarios = daoHorarioAtencion.listarTodos();

        for (HorarioAtencion existente : horarios) {

            // Al modificar, no comparar el horario consigo mismo.
            if (existente.getIdHorario() == idHorarioExcluir) {
                continue;
            }

            boolean mismoVeterinario =
                    nuevo.getVeterinario().getIdUsuario()
                            == existente.getVeterinario().getIdUsuario();

            boolean mismoDia = nuevo.getDia() == existente.getDia();

            if (!mismoVeterinario || !mismoDia) {
                continue;
            }

            boolean seSuperponen =
                    nuevo.getHoraInicio().isBefore(existente.getHoraFin())
                            && nuevo.getHoraFin().isAfter(existente.getHoraInicio());

            if (seSuperponen) {
                throw new RuntimeException(
                        "El veterinario ya tiene un horario el "
                                + existente.getDia()
                                + " de " + existente.getHoraInicio()
                                + " a " + existente.getHoraFin()
                                + ". El horario ingresado se superpone."
                );
            }
        }
    }


    @Override
    public int insertar(HorarioAtencion horarioAtencion) {
        validarHorarioAtencion(horarioAtencion);
        validarSuperposicion(horarioAtencion, 0);

        try {
            int resultado = daoHorarioAtencion.insertar(horarioAtencion);
            TransactionContext.commit();
            return resultado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new RuntimeException(
                    "Error al insertar el horario: " + ex.getMessage(), ex
            );

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public int modificar(HorarioAtencion horarioAtencion) {
        validarHorarioAtencion(horarioAtencion);

        if (horarioAtencion.getIdHorario() <= 0) {
            throw new RuntimeException(
                    "El identificador del horario no es válido"
            );
        }

        validarSuperposicion(
                horarioAtencion, horarioAtencion.getIdHorario()
        );

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

    @Override
    public void asignarHorarioVeterinarioSP(String dni, String dia, java.sql.Time horaInicio, java.sql.Time horaFin) {
        try {
            daoHorarioAtencion.asignarHorarioVeterinarioSP(dni, dia, horaInicio, horaFin);
            TransactionContext.commit();
        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new RuntimeException("Error en asignarHorarioVeterinarioSP: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }
}
