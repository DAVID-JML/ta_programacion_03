package pe.com.mascovet.cita.boi;

import pe.com.mascovet.cita.bo.ICitaBO;
import pe.com.mascovet.cita.dao.CitaDAO;
import pe.com.mascovet.cita.dao.HorarioAtencionDAO;
import pe.com.mascovet.cita.impl.CitaImpl;
import pe.com.mascovet.cita.impl.HorarioAtencionImpl;
import pe.com.mascovet.cita.model.Cita;
import pe.com.mascovet.cita.model.HorarioAtencion;
import pe.com.mascovet.enums.model.Dia;
import pe.com.mascovet.enums.model.EstadoCita;

import java.time.LocalDate;
import java.util.List;

public class CitaBOImpl implements ICitaBO {

    private CitaDAO daoCita;
    private HorarioAtencionDAO daoHorarioAtencion;
    public CitaBOImpl() {
        daoCita = new CitaImpl();
        daoHorarioAtencion = new HorarioAtencionImpl();
    }

    private void validarCita(Cita cita) {
        if (cita == null)
            throw new RuntimeException("La cita que se quiere registrar es null");
        if (cita.getMascota() == null || cita.getMascota().getIdMascota() <= 0)
            throw new RuntimeException("La mascota asociada a la cita no es válida");
        if (cita.getCliente() == null || cita.getCliente().getIdUsuario() <= 0)
            throw new RuntimeException("El cliente asociado a la cita no es válido");
        if (cita.getRecepcionista() != null && cita.getRecepcionista().getIdUsuario() <= 0)
            throw new RuntimeException("El recepcionista asociado a la cita no es válido");
        if (cita.getVeterinario() == null || cita.getVeterinario().getIdUsuario() <= 0)
            throw new RuntimeException("El veterinario asociado a la cita no es válido");
        if (cita.getFecha() == null)
            throw new RuntimeException("La fecha de la cita es obligatoria");
        if (cita.getHora() == null)
            throw new RuntimeException("La hora de la cita es obligatoria");
        if (cita.getEstado() == null)
            throw new RuntimeException("El estado de la cita es obligatorio");
    }

    private void validarTransicionEstado(EstadoCita actual, EstadoCita nuevo) {

        if (actual == null || nuevo == null) {
            throw new RuntimeException(
                    "Los estados de la cita son obligatorios"
            );
        }

        // Modificar otros datos sin cambiar el estado está permitido.
        if (actual == nuevo) {
            return;
        }

        boolean permitido = false;

        switch (actual) {
            case RESERVADA:
                permitido = nuevo == EstadoCita.CONFIRMADA || nuevo == EstadoCita.CANCELADA;
                break;

            case CONFIRMADA:
                permitido = nuevo == EstadoCita.ATENDIDA || nuevo == EstadoCita.CANCELADA;
                break;

            case ATENDIDA:
            case CANCELADA:
                permitido = false;
                break;
        }

        if (!permitido) {
            throw new RuntimeException(
                    "No se puede cambiar una cita de " + actual + " a " + nuevo
            );
        }
    }


    private boolean esCitaActiva(Cita cita) {
        return cita.getEstado() == EstadoCita.RESERVADA
                || cita.getEstado() == EstadoCita.CONFIRMADA;
    }

    private LocalDate obtenerFecha(Cita cita) {
        return new java.sql.Date(cita.getFecha().getTime())
                .toLocalDate();
    }

    private Dia obtenerDia(LocalDate fecha) {
        switch (fecha.getDayOfWeek()) {
            case MONDAY:
                return Dia.LUNES;
            case TUESDAY:
                return Dia.MARTES;
            case WEDNESDAY:
                return Dia.MIERCOLES;
            case THURSDAY:
                return Dia.JUEVES;
            case FRIDAY:
                return Dia.VIERNES;
            case SATURDAY:
                return Dia.SABADO;
            case SUNDAY:
                return Dia.DOMINGO;
            default:
                throw new RuntimeException(
                        "Día de la semana no válido");
        }
    }

    private void validarDisponibilidad(Cita nueva, int idCitaExcluir) {

        if (!esCitaActiva(nueva)) {
            return;
        }

        int idVeterinario = nueva.getVeterinario().getIdUsuario();
        LocalDate fecha = obtenerFecha(nueva);
        Dia dia = obtenerDia(fecha);

        // Paso 1: comprobar que el veterinario atienda a esa hora.
        boolean dentroDelHorario = false;

        for (HorarioAtencion horario :
                daoHorarioAtencion.listarTodos()) {

            boolean mismoVeterinario =
                    horario.getVeterinario().getIdUsuario()
                            == idVeterinario;

            boolean mismoDia = horario.getDia() == dia;

            boolean horaValida =
                    !nueva.getHora().isBefore(horario.getHoraInicio())
                            && nueva.getHora().isBefore(horario.getHoraFin());

            if (mismoVeterinario && mismoDia && horaValida) {
                dentroDelHorario = true;
                break;
            }
        }

        if (!dentroDelHorario) {
            throw new RuntimeException(
                    "El veterinario no atiende en el día "
                            + "y hora seleccionados");
        }

        // Paso 2: comprobar que no tenga otra cita activa a esa hora.
        for (Cita existente : daoCita.listarTodos()) {

            // Al modificar, excluimos la propia cita.
            if (existente.getIdCita() == idCitaExcluir) {
                continue;
            }

            if (!esCitaActiva(existente)) {
                continue;
            }

            if (existente.getVeterinario() == null) {
                continue;
            }

            boolean mismoVeterinario =
                    existente.getVeterinario().getIdUsuario()
                            == idVeterinario;

            boolean mismaFecha =
                    obtenerFecha(existente).equals(fecha);

            boolean mismaHora =
                    existente.getHora().equals(nueva.getHora());

            if (mismoVeterinario && mismaFecha && mismaHora) {
                throw new RuntimeException(
                        "El veterinario ya tiene una cita activa "
                                + "en la fecha y hora seleccionadas");
            }
        }
    }

    @Override
    public int insertar(Cita cita) {

        if (cita == null) {
            throw new RuntimeException(
                    "La cita que se quiere registrar es null"
            );
        }

        // RF020: toda cita nueva comienza como reservada.
        cita.setEstado(EstadoCita.RESERVADA);

        validarCita(cita);

        // RF017: comprobar disponibilidad antes de guardar.
        validarDisponibilidad(cita, 0);

        return daoCita.insertar(cita);
    }

    @Override
    public int modificar(Cita cita) {
        validarCita(cita);

        if (cita.getIdCita() <= 0) {
            throw new RuntimeException(
                    "El identificador de la cita no es válido"
            );
        }

        // Consultar el estado que realmente está guardado.
        Cita citaActual = daoCita.obtenerPorId(cita.getIdCita());

        if (citaActual == null) {
            throw new RuntimeException(
                    "No existe la cita que se quiere modificar"
            );
        }

        if (cita.getEstado() == EstadoCita.ATENDIDA
                && citaActual.getEstado() != EstadoCita.ATENDIDA) {

            throw new RuntimeException(
                    "Para marcar la cita como atendida debe "
                            + "registrarse una atención médica"
            );
        }

        // RF020: comprobar que el cambio de estado esté permitido.
        validarTransicionEstado(
                citaActual.getEstado(),
                cita.getEstado()
        );

        // RF017: comprobar disponibilidad.
        validarDisponibilidad(cita, cita.getIdCita());

        return daoCita.modificar(cita);
    }

    @Override
    public int eliminar(int idCita) {
        if (idCita <= 0) {
            throw new RuntimeException(
                    "El identificador de la cita no es válido"
            );
        }

        int resultado = daoCita.eliminar(idCita);

        if (resultado == 0) {
            throw new RuntimeException(
                    "La cita no existe o ya fue eliminada"
            );
        }

        return resultado;
    }

    @Override
    public List<Cita> listarTodos() {
        return daoCita.listarTodos();
    }

    @Override
    public Cita obtenerPorId(int idCita) {
        if (idCita <= 0)
            throw new RuntimeException("El identificador de la cita no es válido");
        return daoCita.obtenerPorId(idCita);
    }
}
