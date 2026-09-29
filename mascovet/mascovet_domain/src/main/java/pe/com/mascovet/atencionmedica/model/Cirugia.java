package pe.com.mascovet.atencionmedica.model;

import pe.com.mascovet.cita.model.Cita;
import pe.com.mascovet.mascota.model.Mascota;
import pe.com.mascovet.receta.model.Receta;

import java.time.LocalTime;

public class Cirugia extends AtencionMedica{
    private String procedimiento;
    private String indicacionesPostOperatorias;

    public Cirugia() {
    }

    public Cirugia(int idAtencion, String observaciones, double pesoActual, String alergiasIdentificadas,
                   LocalTime horaInicio, LocalTime horaFin, Mascota mascota, Cita cita, Receta receta,
                   String procedimiento, String indicacionesPostOperatorias) {
        super(idAtencion, observaciones, pesoActual, alergiasIdentificadas, horaInicio, horaFin, mascota, cita, receta);
        this.procedimiento = procedimiento;
        this.indicacionesPostOperatorias = indicacionesPostOperatorias;
    }

    public String getProcedimiento() {
        return procedimiento;
    }

    public void setProcedimiento(String procedimiento) {
        this.procedimiento = procedimiento;
    }

    public String getIndicacionesPostOperatorias() {
        return indicacionesPostOperatorias;
    }

    public void setIndicacionesPostOperatorias(String indicacionesPostOperatorias) {
        this.indicacionesPostOperatorias = indicacionesPostOperatorias;
    }
}
