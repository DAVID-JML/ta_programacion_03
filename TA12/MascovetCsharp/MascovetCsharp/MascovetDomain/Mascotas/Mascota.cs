using System;
using System.Collections.Generic;
using MascovetDomain.AtencionesMedicas;
using MascovetDomain.Citas;
using MascovetDomain.Enums;
using MascovetDomain.Usuarios;

namespace MascovetDomain.Mascotas
{
    public class Mascota
    {
        private int idMascota;
        private Cliente? cliente;
        private string? nombre;
        private DateTime? fechaNacimiento;
        private Especie? especie;
        private string? raza;
        private Sexo? sexo;
        private readonly List<Cita> citas = new List<Cita>();
        private readonly List<AtencionMedica> atencionesMedicas = new List<AtencionMedica>();
        private bool activo = true;

        public Mascota() { }

        public int IdMascota { get => idMascota; set => idMascota = value; }
        public Cliente? Cliente { get => cliente; set => cliente = value; }
        public string? Nombre { get => nombre; set => nombre = value; }
        public DateTime? FechaNacimiento { get => fechaNacimiento; set => fechaNacimiento = value; }
        public Especie? Especie { get => especie; set => especie = value; }
        public string? Raza { get => raza; set => raza = value; }
        public Sexo? Sexo { get => sexo; set => sexo = value; }
        public List<Cita> Citas { get => citas; }
        public List<AtencionMedica> AtencionesMedicas { get => atencionesMedicas; }
        public bool Activo { get => activo; set => activo = value; }

        public void AgregarCita(Cita cita) { citas.Add(cita); }
        public void AgregarAtencionMedica(AtencionMedica atencionMedica) { atencionesMedicas.Add(atencionMedica); }
    }
}
