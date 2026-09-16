package pe.com.mascovet.usuario.model;

import pe.com.mascovet.cita.model.Cita;

import java.util.ArrayList;
import java.util.List;

public class Recepcionista extends Usuario{
    private String turno;
    private final List<Cita> citas;

    public Recepcionista() {
        citas = new ArrayList<>();
    }

    public Recepcionista(int idUsuario, String dni, String nombre, String apellido,
                         String nombreUsuario, String contrasena, boolean activo,
                         String turno) {
        super();
        this.turno = turno;
        citas = new ArrayList<>();
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public List<Cita> getCitas() {
        return citas;
    }

    public void agregarCita(Cita cita) {
        citas.add(cita);
    }
}
