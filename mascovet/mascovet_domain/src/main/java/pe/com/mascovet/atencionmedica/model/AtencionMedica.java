package pe.com.mascovet.atencionmedica.model;

import pe.com.mascovet.cita.model.Cita;
import pe.com.mascovet.mascota.model.Mascota;
import pe.com.mascovet.receta.model.Receta;

import java.time.LocalTime;

public abstract class AtencionMedica {
    private int idAtencion;
    private String observaciones;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Mascota mascota;
    private Cita cita;
    private Receta receta;

    public AtencionMedica() {
    }

    public AtencionMedica(int idAtencion, String observaciones,
                          LocalTime horaInicio, LocalTime horaFin,
                          Mascota mascota, Cita cita) {
        this.idAtencion = idAtencion;
        this.observaciones = observaciones;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.mascota = mascota;
        this.cita = cita;
    }

    public int getIdAtencion() {
        return idAtencion;
    }

    public void setIdAtencion(int idAtencion) {
        this.idAtencion = idAtencion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public Receta getReceta() {
        return receta;
    }

    public void setReceta(Receta receta) {
        this.receta = receta;
    }
}
