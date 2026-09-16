package pe.com.mascovet.usuario.model;

import pe.com.mascovet.cita.model.Cita;
import pe.com.mascovet.cita.model.HorarioAtencion;
import pe.com.mascovet.enums.model.Especialidad;

import java.util.ArrayList;
import java.util.List;

public class Veterinario extends Usuario {
    private String numeroColegiatura;
    private Especialidad especialidad;
    private final List<Cita> citas;
    private final List<HorarioAtencion> horariosAtencion;

    public Veterinario() {
        citas = new ArrayList<>();
        horariosAtencion = new ArrayList<>();
    }

    public Veterinario(int idUsuario, String dni, String nombre, String apellido,
                       String nombreUsuario, String contrasena, boolean activo,
                       String numeroColegiatura, Especialidad especialidad) {
        super(idUsuario, dni, nombre, apellido, nombreUsuario, contrasena, activo);
        this.numeroColegiatura = numeroColegiatura;
        this.especialidad = especialidad;
        citas = new ArrayList<>();
        horariosAtencion = new ArrayList<>();
    }

    public String getNumeroColegiatura() {
        return numeroColegiatura;
    }

    public void setNumeroColegiatura(String numeroColegiatura) {
        this.numeroColegiatura = numeroColegiatura;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public List<Cita> getCitas() {
        return citas;
    }

    public List<HorarioAtencion> getHorariosAtencion() {
        return horariosAtencion;
    }

    public void agregarCita(Cita cita) {
        citas.add(cita);
    }

    public void agregarHorarioAtencion(HorarioAtencion horarioAtencion) {
        horariosAtencion.add(horarioAtencion);
    }
}
