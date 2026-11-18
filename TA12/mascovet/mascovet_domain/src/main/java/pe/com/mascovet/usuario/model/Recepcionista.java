package pe.com.mascovet.usuario.model;

import pe.com.mascovet.cita.model.Cita;

import java.util.ArrayList;
import java.util.List;

public class Recepcionista extends Usuario{
    private String turno;
    private String correo;
    private String telefono;
    private final List<Cita> citas;

    public Recepcionista() {
        citas = new ArrayList<>();
    }

    public Recepcionista(int idUsuario, String dni, String nombre, String apellidoPaterno, String apellidoMaterno,
                         String nombreUsuario, String contrasena, boolean activo,
                         String turno) {
        super(idUsuario, dni, nombre, apellidoPaterno, apellidoMaterno,
                nombreUsuario, contrasena, activo);
        this.turno = turno;
        citas = new ArrayList<>();
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<Cita> getCitas() {
        return citas;
    }

    public void agregarCita(Cita cita) {
        citas.add(cita);
    }
}
