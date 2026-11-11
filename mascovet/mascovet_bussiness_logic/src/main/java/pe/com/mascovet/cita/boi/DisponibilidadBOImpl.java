package pe.com.mascovet.cita.boi;

import pe.com.mascovet.cita.bo.IDisponibilidadBO;
import pe.com.mascovet.cita.dao.CitaDAO;
import pe.com.mascovet.cita.dao.HorarioAtencionDAO;
import pe.com.mascovet.cita.impl.CitaImpl;
import pe.com.mascovet.cita.impl.HorarioAtencionImpl;
import pe.com.mascovet.cita.model.Cita;
import pe.com.mascovet.cita.model.HorarioAtencion;
import pe.com.mascovet.enums.model.Dia;
import pe.com.mascovet.enums.model.EstadoCita;
import pe.com.mascovet.usuario.dao.VeterinarioDAO;
import pe.com.mascovet.usuario.impl.VeterinarioImpl;
import pe.com.mascovet.usuario.model.Veterinario;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DisponibilidadBOImpl implements IDisponibilidadBO {

    private static final int DURACION_TURNO_MIN = 30;

    private HorarioAtencionDAO daoHorarioAtencion;
    private CitaDAO daoCita;
    private VeterinarioDAO daoVeterinario;

    public DisponibilidadBOImpl() {
        daoHorarioAtencion = new HorarioAtencionImpl();
        daoCita = new CitaImpl();
        daoVeterinario = new VeterinarioImpl();
    }

    private LocalDate validarFecha(String fechaTexto) {
        if (fechaTexto == null || fechaTexto.trim().isEmpty())
            throw new RuntimeException("La fecha es obligatoria");
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fechaTexto.trim());
        } catch (DateTimeParseException e) {
            throw new RuntimeException("La fecha debe tener formato yyyy-MM-dd");
        }
        if (fecha.isBefore(LocalDate.now()))
            throw new RuntimeException("No se puede consultar disponibilidad en fechas pasadas");
        return fecha;
    }

    @Override
    public List<HorarioAtencion> listarDisponibles(String fechaTexto, String especialidad, int idVeterinario) {
        LocalDate fecha = validarFecha(fechaTexto);
        Dia dia = Dia.values()[fecha.getDayOfWeek().getValue() - 1]; // LUNES=0 ... DOMINGO=6

        Map<Integer, Veterinario> veterinarios = new HashMap<>();
        for (Veterinario v : daoVeterinario.listarTodos())
            if (v.isActivo()) veterinarios.put(v.getIdUsuario(), v);

        // Turnos ocupados ese dia: "idVeterinario|HH:mm" (citas activas, no canceladas ni atendidas)
        Set<String> ocupados = new HashSet<>();
        for (Cita c : daoCita.listarTodos()) {
            if (!c.isActivo() || c.getVeterinario() == null || c.getFecha() == null || c.getHora() == null)
                continue;
            if (c.getEstado() == EstadoCita.CANCELADA || c.getEstado() == EstadoCita.ATENDIDA) continue;
            LocalDate fechaCita = Instant.ofEpochMilli(c.getFecha().getTime())
                    .atZone(ZoneId.systemDefault()).toLocalDate();
            if (fechaCita.equals(fecha))
                ocupados.add(c.getVeterinario().getIdUsuario() + "|" + c.getHora().withSecond(0).withNano(0));
        }

        List<HorarioAtencion> turnos = new ArrayList<>();
        for (HorarioAtencion h : daoHorarioAtencion.listarTodos()) {
            if (!h.isActivo() || h.getVeterinario() == null || h.getDia() != dia) continue;
            Veterinario vet = veterinarios.get(h.getVeterinario().getIdUsuario());
            if (vet == null) continue; // inactivo o inexistente
            if (idVeterinario > 0 && vet.getIdUsuario() != idVeterinario) continue;
            if (especialidad != null && !especialidad.trim().isEmpty()
                    && (vet.getEspecialidad() == null
                    || !vet.getEspecialidad().name().equalsIgnoreCase(especialidad.trim())))
                continue;

            for (LocalTime t = h.getHoraInicio();
                 !t.plusMinutes(DURACION_TURNO_MIN).isAfter(h.getHoraFin());
                 t = t.plusMinutes(DURACION_TURNO_MIN)) {
                if (LocalDateTime.of(fecha, t).isBefore(LocalDateTime.now())) continue; // hora pasada
                if (ocupados.contains(vet.getIdUsuario() + "|" + t.withSecond(0).withNano(0))) continue;
                HorarioAtencion turno = new HorarioAtencion(dia, t, t.plusMinutes(DURACION_TURNO_MIN));
                turno.setVeterinario(vet);
                turnos.add(turno);
            }
        }
        turnos.sort(Comparator.comparing(HorarioAtencion::getHoraInicio)
                .thenComparing(t -> t.getVeterinario().getNombre()));
        return turnos;
    }
}
