using System;
using System.Collections.Generic;
using MascovetDomain.Citas;
using MascovetDomain.Recetas;

namespace MascovetDomain.AtencionesMedicas
{
    public class Consulta : AtencionMedica
    {
        private string? motivoConsulta;
        private string? diagnostico;
        private string? tratamiento;

        public Consulta() { }

        public Consulta(int idAtencion, string? observaciones, TimeOnly? horaInicio, TimeOnly? horaFin, Cita? cita, Receta? receta, double pesoActual, string? alergias, string? motivoConsulta, string? diagnostico, string? tratamiento) : base(idAtencion, observaciones, horaInicio, horaFin, cita, receta, pesoActual, alergias)
        {
            this.motivoConsulta = motivoConsulta;
            this.diagnostico = diagnostico;
            this.tratamiento = tratamiento;
        }

        public string? MotivoConsulta { get => motivoConsulta; set => motivoConsulta = value; }
        public string? Diagnostico { get => diagnostico; set => diagnostico = value; }
        public string? Tratamiento { get => tratamiento; set => tratamiento = value; }
    }
}
