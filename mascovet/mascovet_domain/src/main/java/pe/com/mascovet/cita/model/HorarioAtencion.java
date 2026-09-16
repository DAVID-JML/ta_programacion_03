package pe.com.mascovet.cita.model;

import pe.com.mascovet.enums.model.Dia;
import pe.com.mascovet.usuario.model.Veterinario;

import java.time.LocalTime;


public class HorarioAtencion {
    private int idHorario;
    private Veterinario veterinario;
    private Dia dia;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    public HorarioAtencion() {
    }

    public HorarioAtencion(Dia dia, LocalTime horaInicio, LocalTime horaFin) {
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public int getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(int idHorario) {
        this.idHorario = idHorario;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }

    public Dia getDia() {
        return dia;
    }

    public void setDia(Dia dia) {
        this.dia = dia;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }
}
