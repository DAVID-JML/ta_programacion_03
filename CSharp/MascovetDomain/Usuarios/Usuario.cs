using System;
using System.Collections.Generic;

namespace MascovetDomain.Usuarios
{
    public abstract class Usuario
    {
        private int idUsuario;
        private string? dni;
        private string? nombre;
        private string? apellidoPaterno;
        private string? apellidoMaterno;
        private string? nombreUsuario;
        private string? contrasena;
        private bool activo = true;

        public Usuario() { }

        public Usuario(int idUsuario, string? dni, string? nombre, string? apellidoPaterno, string? apellidoMaterno, string? nombreUsuario, string? contrasena, bool activo)
        {
            this.idUsuario = idUsuario;
            this.dni = dni;
            this.nombre = nombre;
            this.apellidoPaterno = apellidoPaterno;
            this.apellidoMaterno = apellidoMaterno;
            this.nombreUsuario = nombreUsuario;
            this.contrasena = contrasena;
            this.activo = activo;
        }

        public int IdUsuario { get => idUsuario; set => idUsuario = value; }
        public string? Dni { get => dni; set => dni = value; }
        public string? Nombre { get => nombre; set => nombre = value; }
        public string? ApellidoPaterno { get => apellidoPaterno; set => apellidoPaterno = value; }
        public string? ApellidoMaterno { get => apellidoMaterno; set => apellidoMaterno = value; }
        public string? NombreUsuario { get => nombreUsuario; set => nombreUsuario = value; }
        public string? Contrasena { get => contrasena; set => contrasena = value; }
        public bool Activo { get => activo; set => activo = value; }
        public string NombreCompleto { get => $"{nombre} {apellidoPaterno} {apellidoMaterno}".Trim(); }
    }
}
