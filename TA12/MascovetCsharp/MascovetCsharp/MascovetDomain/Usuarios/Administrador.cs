using System;
using System.Collections.Generic;

namespace MascovetDomain.Usuarios
{
    public class Administrador : Usuario
    {
        private string? cargo;

        public Administrador() { }

        public Administrador(int idUsuario, string? dni, string? nombre, string? apellidoPaterno, string? apellidoMaterno, string? nombreUsuario, string? contrasena, bool activo, string? cargo) : base(idUsuario, dni, nombre, apellidoPaterno, apellidoMaterno, nombreUsuario, contrasena, activo)
        {
            this.cargo = cargo;
        }

        public string? Cargo { get => cargo; set => cargo = value; }
    }
}
