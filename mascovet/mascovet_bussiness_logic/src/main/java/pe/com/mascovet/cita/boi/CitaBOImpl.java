package pe.com.mascovet.cita.boi;

import pe.com.mascovet.cita.bo.ICitaBO;
import pe.com.mascovet.cita.dao.CitaDAO;
import pe.com.mascovet.cita.impl.CitaImpl;
import pe.com.mascovet.cita.model.Cita;
import pe.com.mascovet.cita.dao.HorarioAtencionDAO;
import pe.com.mascovet.cita.impl.HorarioAtencionImpl;
import pe.com.mascovet.cita.model.HorarioAtencion;
import pe.com.mascovet.enums.model.Dia;
import pe.com.mascovet.enums.model.EstadoCita;
import pe.com.mascovet.mascota.dao.MascotaDAO;
import pe.com.mascovet.mascota.impl.MascotaImpl;
import pe.com.mascovet.mascota.model.Mascota;
import pe.com.mascovet.usuario.dao.ClienteDAO;
import pe.com.mascovet.usuario.impl.ClienteImpl;
import pe.com.mascovet.usuario.model.Cliente;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class CitaBOImpl implements ICitaBO {

    private CitaDAO daoCita;
    private HorarioAtencionDAO daoHorarioAtencion;
    private MascotaDAO daoMascota;
    private ClienteDAO daoCliente;

    public CitaBOImpl() {
        daoCita = new CitaImpl();
        daoHorarioAtencion = new HorarioAtencionImpl();
        daoMascota = new MascotaImpl();
        daoCliente = new ClienteImpl();
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
        if (cita.getTipoAtencion() == null)
            throw new RuntimeException("El tipo de atención es obligatorio");
        if (cita.getMotivoCancelacion() != null && cita.getMotivoCancelacion().length() > 300)
            throw new RuntimeException("El motivo de cancelación no debe exceder los 300 caracteres");
        if (cita.getEstado() == EstadoCita.CANCELADA
                && (cita.getMotivoCancelacion() == null || cita.getMotivoCancelacion().trim().isEmpty()))
            throw new RuntimeException("Debe indicar el motivo de cancelación");
    }

    private void validarMascotaYClienteActivos(Cita cita) {
        Mascota mascota = daoMascota.obtenerPorId(cita.getMascota().getIdMascota());
        if (mascota == null || !mascota.isActivo())
            throw new RuntimeException("La mascota no existe o está inactiva");
        if (mascota.getCliente() == null)
            throw new RuntimeException("La mascota no tiene un cliente asociado");
        Cliente cliente = daoCliente.obtenerPorId(mascota.getCliente().getIdUsuario());
        if (cliente == null || !cliente.isActivo())
            throw new RuntimeException("El cliente no existe o está inactivo");
        if (cliente.getIdUsuario() != cita.getCliente().getIdUsuario())
            throw new RuntimeException("La mascota no pertenece al cliente indicado");
    }

    private LocalDate fechaLocal(Cita cita) {
        return java.time.Instant.ofEpochMilli(cita.getFecha().getTime())
                .atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private void validarFechaFutura(Cita cita) {
        if (LocalDateTime.of(fechaLocal(cita), cita.getHora()).isBefore(LocalDateTime.now()))
            throw new RuntimeException("No se permiten citas en fechas u horas pasadas");
    }

    private void validarDisponibilidad(Cita cita) {
        int idVeterinario = cita.getVeterinario().getIdUsuario();

        Dia dia = Dia.values()[
                fechaLocal(cita).getDayOfWeek().getValue() - 1
                ];

        boolean dentroHorario = false;

        for (HorarioAtencion horario : daoHorarioAtencion.listarTodos()) {
            System.out.println(
                    "Horario registrado -> Veterinario: "
                            + horario.getVeterinario().getIdUsuario()
                            + " | Dia: " + horario.getDia()
                            + " | Inicio: " + horario.getHoraInicio()
                            + " | Fin: " + horario.getHoraFin()
            );
            if (horario.getVeterinario().getIdUsuario() == idVeterinario
                    && horario.getDia() == dia
                    && !cita.getHora().isBefore(horario.getHoraInicio())
                    && cita.getHora().isBefore(horario.getHoraFin())) {
                dentroHorario = true;
                break;
            }
        }

        if (!dentroHorario)
            throw new RuntimeException(
                    "La cita está fuera del horario de atención del veterinario"
            );

        for (Cita existente : daoCita.listarTodos()) {
            if (existente.getIdCita() == cita.getIdCita()) continue;
            if (existente.getVeterinario() == null) continue;
            if (!existente.isActivo() || existente.getEstado() == EstadoCita.CANCELADA
                    || existente.getEstado() == EstadoCita.ATENDIDA) continue;
            if (existente.getVeterinario().getIdUsuario() == idVeterinario
                    && fechaLocal(existente).equals(fechaLocal(cita))
                    && existente.getHora().equals(cita.getHora()))
                throw new RuntimeException("El veterinario ya tiene una cita activa a esa fecha y hora");
        }
    }

    private void validarCambioEstado(EstadoCita anterior, EstadoCita nuevo) {
        if (anterior == nuevo) return;
        if (anterior == EstadoCita.RESERVADA
                && (nuevo == EstadoCita.CONFIRMADA || nuevo == EstadoCita.CANCELADA)) return;
        if (anterior == EstadoCita.CONFIRMADA
                && (nuevo == EstadoCita.ATENDIDA || nuevo == EstadoCita.CANCELADA)) return;
        throw new RuntimeException("Transición de estado de cita no permitida: " + anterior + " a " + nuevo);
    }

    @Override
    public int insertar(Cita cita) {
        validarCita(cita);
        if (cita.getEstado() != EstadoCita.RESERVADA)
            throw new RuntimeException("Una cita nueva debe comenzar en estado RESERVADA");
        validarFechaFutura(cita);
        validarMascotaYClienteActivos(cita);
        validarDisponibilidad(cita);
        return daoCita.insertar(cita);
    }

    @Override
    public int modificar(Cita cita) {
        validarCita(cita);
        if (cita.getIdCita() <= 0)
            throw new RuntimeException("El identificador de la cita no es válido");
        Cita anterior = daoCita.obtenerPorId(cita.getIdCita());
        if (anterior == null)
            throw new RuntimeException("La cita no existe o está inactiva");
        if (anterior.getEstado() == EstadoCita.ATENDIDA || anterior.getEstado() == EstadoCita.CANCELADA)
            throw new RuntimeException("No se puede modificar una cita atendida o cancelada");
        validarCambioEstado(anterior.getEstado(), cita.getEstado());
        boolean cambiaReserva =
                !fechaLocal(anterior).equals(fechaLocal(cita))
                        || !anterior.getHora().equals(cita.getHora())
                        || anterior.getVeterinario().getIdUsuario()
                        != cita.getVeterinario().getIdUsuario();
        if (cambiaReserva) {
            if (anterior.getEstado() != EstadoCita.RESERVADA)
                throw new RuntimeException("Solo se pueden reprogramar citas reservadas");
            validarFechaFutura(cita);
            validarMascotaYClienteActivos(cita);
            validarDisponibilidad(cita);
        } else if (cita.getEstado() == EstadoCita.CONFIRMADA
                && anterior.getEstado() == EstadoCita.RESERVADA) {
            validarFechaFutura(cita);
        }
        return daoCita.modificar(cita);
    }

    @Override
    public int eliminar(int idCita) {
        if (idCita <= 0)
            throw new RuntimeException("El identificador de la cita no es válido");
        return daoCita.eliminar(idCita);
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
