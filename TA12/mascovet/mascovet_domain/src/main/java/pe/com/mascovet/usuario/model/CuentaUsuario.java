package pe.com.mascovet.usuario.model;

public class CuentaUsuario {
    private int idCuentaUsuario;
    private Usuario usuario;
    private int idUsuario;
    private String username;
    private String password;
    private boolean activo;
    private String rol;
    private String nombre;
    private String usuarioVinculado;
     
    private String nuevaPassword;

    public int getIdCuentaUsuario() { return idCuentaUsuario; }
    public void setIdCuentaUsuario(int idCuentaUsuario) { this.idCuentaUsuario = idCuentaUsuario; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getNuevaPassword() { return nuevaPassword; }
    public void setNuevaPassword(String nuevaPassword) { this.nuevaPassword = nuevaPassword; }
    public String getUsuarioVinculado() { return usuarioVinculado; }
    public void setUsuarioVinculado(String usuarioVinculado) { this.usuarioVinculado = usuarioVinculado; }
}
