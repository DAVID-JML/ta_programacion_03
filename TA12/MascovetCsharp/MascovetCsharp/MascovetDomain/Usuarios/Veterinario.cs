using System;
using System.Collections.Generic;
using MascovetDomain.Citas;
using MascovetDomain.Enums;

namespace MascovetDomain.Usuarios
{
    public class Veterinario : Usuario
    {
        private string? numeroColegiatura;
        private Especialidad? especialidad;
        private string? correo;
        private string? telefono;
        private readonly List<Cita> citas = new List<Cita>();
        private readonly List<HorarioAtencion> horariosAtencion = new List<HorarioAtencion>();

        public Veterinario() { }

        public Veterinario(int idUsuario, string? dni, string? nombre, string? apellidoPaterno, string? apellidoMaterno, string? nombreUsuario, string? contrasena, bool activo, string? numeroColegiatura, Especialidad? especialidad) : base(idUsuario, dni, nombre, apellidoPaterno, apellidoMaterno, nombreUsuario, contrasena, activo)
        {
            this.numeroColegiatura = numeroColegiatura;
            this.especialidad = especialidad;
        }

        public string? NumeroColegiatura { get => numeroColegiatura; set => numeroColegiatura = value; }
        public Especialidad? Especialidad { get => especialidad; set => especialidad = value; }
        public string? Correo { get => correo; set => correo = value; }
        public string? Telefono { get => telefono; set => telefono = value; }
        public List<Cita> Citas { get => citas; }
        public List<HorarioAtencion> HorariosAtencion { get => horariosAtencion; }

        public void AgregarCita(Cita cita) { citas.Add(cita); }
        public void AgregarHorarioAtencion(HorarioAtencion horarioAtencion) { horariosAtencion.Add(horarioAtencion); }
    }
}
