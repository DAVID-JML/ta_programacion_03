package pe.com.mascovet.usuario.model;

public abstract class Usuario {
    private int idUsuario;
    private String dni;
    private String nombre;
    private String apellido;
    private String nombreUsuario;
    private String contrasena;
    private boolean activo;

    public Usuario() {
    }

    public Usuario(int idUsuario, String dni, String nombre, String apellido,
                   String nombreUsuario, String contrasena, boolean activo) {
        this.idUsuario = idUsuario;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.activo = activo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    public boolean iniciarSesion(String nombreUsuario, String contrasena) {
        return activo
                && this.nombreUsuario.equals(nombreUsuario)
                && this.contrasena.equals(contrasena);
    }

}
