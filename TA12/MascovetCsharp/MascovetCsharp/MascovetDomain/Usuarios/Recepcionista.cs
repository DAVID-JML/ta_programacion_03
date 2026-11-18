using System;
using System.Collections.Generic;
using MascovetDomain.Citas;

namespace MascovetDomain.Usuarios
{
    public class Recepcionista : Usuario
    {
        private string? turno;
        private string? correo;
        private string? telefono;
        private readonly List<Cita> citas = new List<Cita>();

        public Recepcionista() { }

        public Recepcionista(int idUsuario, string? dni, string? nombre, string? apellidoPaterno, string? apellidoMaterno, string? nombreUsuario, string? contrasena, bool activo, string? turno) : base(idUsuario, dni, nombre, apellidoPaterno, apellidoMaterno, nombreUsuario, contrasena, activo)
        {
            this.turno = turno;
        }

        public string? Turno { get => turno; set => turno = value; }
        public string? Correo { get => correo; set => correo = value; }
        public string? Telefono { get => telefono; set => telefono = value; }
        public List<Cita> Citas { get => citas; }

        public void AgregarCita(Cita cita) { citas.Add(cita); }
    }
}
