package pe.com.mascovet.atencionmedica.model;

import pe.com.mascovet.cita.model.Cita;
import pe.com.mascovet.mascota.model.Mascota;
import pe.com.mascovet.receta.model.Receta;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Vacunacion extends AtencionMedica {
    private Date fechaAplicacion;
    private Date fechaProximaDosis;
    private String dosis;
    private final List<Vacuna> vacunas;

    public Vacunacion() {
        vacunas = new ArrayList<>();
    }

    public Vacunacion(int idAtencion, String observaciones, double pesoActual, String alergiasIdentificadas,
                      LocalTime horaInicio, LocalTime horaFin, Mascota mascota, Cita cita, Receta receta,
                      Date fechaAplicacion, Date fechaProximaDosis, String dosis) {
        super(idAtencion, observaciones, pesoActual, alergiasIdentificadas, horaInicio, horaFin, mascota, cita, receta);
        this.fechaAplicacion = fechaAplicacion;
        this.fechaProximaDosis = fechaProximaDosis;
        this.dosis = dosis;
        vacunas = new ArrayList<>();
    }

    public Date getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(Date fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }

    public Date getFechaProximaDosis() {
        return fechaProximaDosis;
    }

    public void setFechaProximaDosis(Date fechaProximaDosis) {
        this.fechaProximaDosis = fechaProximaDosis;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public List<Vacuna> getVacunas() {
        return vacunas;
    }

    public void agregarVacuna(Vacuna vacuna) {
        vacunas.add(vacuna);
    }
}
