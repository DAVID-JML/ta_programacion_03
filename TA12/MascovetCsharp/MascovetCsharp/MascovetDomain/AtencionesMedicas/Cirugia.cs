using System;
using System.Collections.Generic;
using MascovetDomain.Citas;
using MascovetDomain.Recetas;

namespace MascovetDomain.AtencionesMedicas
{
    public class Cirugia : AtencionMedica
    {
        private string? procedimiento;
        private string? indicacionesPostOperatorias;

        public Cirugia() { }

        public Cirugia(int idAtencion, string? observaciones, TimeOnly? horaInicio, TimeOnly? horaFin, Cita? cita, Receta? receta, double pesoActual, string? alergias, string? procedimiento, string? indicacionesPostOperatorias) : base(idAtencion, observaciones, horaInicio, horaFin, cita, receta, pesoActual, alergias)
        {
            this.procedimiento = procedimiento;
            this.indicacionesPostOperatorias = indicacionesPostOperatorias;
        }

        public string? Procedimiento { get => procedimiento; set => procedimiento = value; }
        public string? IndicacionesPostOperatorias { get => indicacionesPostOperatorias; set => indicacionesPostOperatorias = value; }
    }
}
