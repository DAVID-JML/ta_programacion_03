package pe.com.mascovet.usuario.model;

public class Administrador extends Usuario{

    private String cargo;

    public Administrador() {
    }

    public Administrador(int idUsuario, String dni, String nombre,
                         String apellido, String nombreUsuario,
                         String contrasena, boolean activo, String cargo) {

        super(idUsuario, dni, nombre, apellido,
                nombreUsuario, contrasena, activo);

        this.cargo = cargo;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
}
