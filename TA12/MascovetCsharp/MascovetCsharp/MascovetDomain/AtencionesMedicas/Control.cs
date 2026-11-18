using System;
using System.Collections.Generic;
using MascovetDomain.Citas;
using MascovetDomain.Recetas;

namespace MascovetDomain.AtencionesMedicas
{
    public class Control : AtencionMedica
    {
        private string? evolucion;
        private string? indicaciones;

        public Control() { }

        public Control(int idAtencion, string? observaciones, TimeOnly? horaInicio, TimeOnly? horaFin, Cita? cita, Receta? receta, double pesoActual, string? alergias, string? evolucion, string? indicaciones) : base(idAtencion, observaciones, horaInicio, horaFin, cita, receta, pesoActual, alergias)
        {
            this.evolucion = evolucion;
            this.indicaciones = indicaciones;
        }

        public string? Evolucion { get => evolucion; set => evolucion = value; }
        public string? Indicaciones { get => indicaciones; set => indicaciones = value; }
    }
}
