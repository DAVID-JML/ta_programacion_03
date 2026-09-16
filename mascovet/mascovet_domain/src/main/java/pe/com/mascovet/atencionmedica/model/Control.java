package pe.com.mascovet.atencionmedica.model;

import pe.com.mascovet.cita.model.Cita;
import pe.com.mascovet.mascota.model.Mascota;

import java.time.LocalTime;

public class Control extends AtencionMedica {
    private String evolucion;
    private String indicaciones;

    public Control() {
    }

    public Control(int idAtencion, String observaciones,
                   LocalTime horaInicio, LocalTime horaFin,
                   Mascota mascota, Cita cita,
                   String evolucion, String indicaciones) {
        super(idAtencion, observaciones, horaInicio, horaFin, mascota, cita);
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
