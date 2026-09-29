package pe.com.mascovet.atencionmedica.model;

import pe.com.mascovet.cita.model.Cita;
import pe.com.mascovet.mascota.model.Mascota;
import pe.com.mascovet.receta.model.Receta;

import java.time.LocalTime;

public class Consulta extends AtencionMedica {
    private String motivoConsulta;
    private String diagnostico;
    private String tratamiento;

    public Consulta() {
    }

    public Consulta(int idAtencion, String observaciones, double pesoActual, String alergiasIdentificadas,
                    LocalTime horaInicio, LocalTime horaFin, Mascota mascota, Cita cita, Receta receta,
                    String motivoConsulta, String diagnostico, String tratamiento) {
        super(idAtencion, observaciones, pesoActual, alergiasIdentificadas, horaInicio, horaFin, mascota, cita, receta);
        this.motivoConsulta = motivoConsulta;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }
}
