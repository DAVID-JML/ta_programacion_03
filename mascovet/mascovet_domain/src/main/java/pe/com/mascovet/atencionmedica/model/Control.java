package pe.com.mascovet.atencionmedica.model;

import pe.com.mascovet.cita.model.Cita;
import pe.com.mascovet.mascota.model.Mascota;
import pe.com.mascovet.receta.model.Receta;

import java.time.LocalTime;

public class Control extends AtencionMedica {
    private String evolucion;
    private String indicaciones;

    public Control() {
    }

    public Control(int idAtencion, String observaciones, double pesoActual, String alergiasIdentificadas,
                   LocalTime horaInicio, LocalTime horaFin, Mascota mascota, Cita cita, Receta receta, String evolucion,
                   String indicaciones) {
        super(idAtencion, observaciones, pesoActual, alergiasIdentificadas, horaInicio, horaFin, mascota, cita, receta);
        this.evolucion = evolucion;
        this.indicaciones = indicaciones;
    }

    public String getEvolucion() {
        return evolucion;
    }

    public void setEvolucion(String evolucion) {
        this.evolucion = evolucion;
    }

    public String getIndicaciones() {
        return indicaciones;
    }

    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }
}
