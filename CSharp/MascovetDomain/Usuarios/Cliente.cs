using System;
using System.Collections.Generic;
using MascovetDomain.Citas;
using MascovetDomain.Mascotas;

namespace MascovetDomain.Usuarios
{
    public class Cliente : Usuario
    {
        private string? correo;
        private string? telefono;
        private readonly List<Mascota> mascotas = new List<Mascota>();
        private readonly List<Cita> citas = new List<Cita>();

        public Cliente() { }

        public Cliente(int idUsuario, string? dni, string? nombre, string? apellidoPaterno, string? apellidoMaterno, string? nombreUsuario, string? contrasena, bool activo, string? correo, string? telefono) : base(idUsuario, dni, nombre, apellidoPaterno, apellidoMaterno, nombreUsuario, contrasena, activo)
        {
            this.correo = correo;
            this.telefono = telefono;
        }

        public string? Correo { get => correo; set => correo = value; }
        public string? Telefono { get => telefono; set => telefono = value; }
        public List<Mascota> Mascotas { get => mascotas; }
        public List<Cita> Citas { get => citas; }

        public void AgregarMascota(Mascota mascota) { mascotas.Add(mascota); }
        public void AgregarCita(Cita cita) { citas.Add(cita); }
    }
}
