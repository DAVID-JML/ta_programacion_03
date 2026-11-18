namespace MascovetDomain.Usuarios
{
    public class CuentaUsuario
    {
        public int IdCuentaUsuario { get; set; }
        public Usuario? Usuario { get; set; }
        public int IdUsuario { get; set; }
        public string? Username { get; set; }
        public string? Password { get; set; }
        public string? NuevaPassword { get; set; }
        public bool Activo { get; set; }
        public string? Rol { get; set; }
        public string? Nombre { get; set; }
        public string? UsuarioVinculado { get; set; }
    }
}
