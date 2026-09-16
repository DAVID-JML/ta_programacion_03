package pe.com.mascovet.usuario.model;

import pe.com.mascovet.cita.model.Cita;
import pe.com.mascovet.mascota.model.Mascota;

import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario{
    private String correo;
    private String telefono;
    private final List<Mascota> mascotas;
    private final List<Cita> citas;

    public Cliente() {
        mascotas = new ArrayList<>();
        citas = new ArrayList<>();
    }

    public Cliente(int idUsuario, String dni, String nombre, String apellido,
                   String nombreUsuario, String contrasena, boolean activo,
                   String correo, String telefono) {
        super(idUsuario, dni, nombre, apellido, nombreUsuario, contrasena, activo);
        this.correo = correo;
        this.telefono = telefono;
        mascotas = new ArrayList<>();
        citas = new ArrayList<>();
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

    public List<Mascota> getMascotas() {
        return mascotas;
    }

    public List<Cita> getCitas() {
        return citas;
    }

    public void agregarMascota(Mascota mascota) {
        mascotas.add(mascota);
    }

    public void agregarCita(Cita cita) {
        citas.add(cita);
    }
}
