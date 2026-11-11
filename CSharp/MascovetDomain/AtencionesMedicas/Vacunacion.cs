using System;
using System.Collections.Generic;
using MascovetDomain.Citas;
using MascovetDomain.Recetas;

namespace MascovetDomain.AtencionesMedicas
{
    public class Vacunacion : AtencionMedica
    {
        private DateTime? fechaAplicacion;
        private DateTime? fechaProximaDosis;
        private string? dosis;
        private readonly List<Vacuna> vacunas = new List<Vacuna>();

        public Vacunacion() { }

        public Vacunacion(int idAtencion, string? observaciones, TimeOnly? horaInicio, TimeOnly? horaFin, Cita? cita, Receta? receta, double pesoActual, string? alergias, DateTime? fechaAplicacion, DateTime? fechaProximaDosis, string? dosis) : base(idAtencion, observaciones, horaInicio, horaFin, cita, receta, pesoActual, alergias)
        {
            this.fechaAplicacion = fechaAplicacion;
            this.fechaProximaDosis = fechaProximaDosis;
            this.dosis = dosis;
        }

        public DateTime? FechaAplicacion { get => fechaAplicacion; set => fechaAplicacion = value; }
        public DateTime? FechaProximaDosis { get => fechaProximaDosis; set => fechaProximaDosis = value; }
        public string? Dosis { get => dosis; set => dosis = value; }
        public List<Vacuna> Vacunas { get => vacunas; }

        public void AgregarVacuna(Vacuna vacuna) { vacunas.Add(vacuna); }
    }
}
