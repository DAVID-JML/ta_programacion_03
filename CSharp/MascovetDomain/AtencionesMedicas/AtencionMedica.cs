using System;
using System.Collections.Generic;
using MascovetDomain.Citas;
using MascovetDomain.Mascotas;
using MascovetDomain.Recetas;

namespace MascovetDomain.AtencionesMedicas
{
    public abstract class AtencionMedica
    {
        private int idAtencion;
        private string? observaciones;
        private TimeOnly? horaInicio;
        private TimeOnly? horaFin;
        private Cita? cita;
        private Receta? receta;
        private double pesoActual;
        private string? alergias;
        private bool activo = true;

        public AtencionMedica() { }

        public AtencionMedica(int idAtencion, string? observaciones, TimeOnly? horaInicio, TimeOnly? horaFin, Cita? cita, Receta? receta, double pesoActual, string? alergias)
        {
            this.idAtencion = idAtencion;
            this.observaciones = observaciones;
            this.horaInicio = horaInicio;
            this.horaFin = horaFin;
            this.cita = cita;
            this.receta = receta;
            this.pesoActual = pesoActual;
            this.alergias = alergias;
        }

        public int IdAtencion { get => idAtencion; set => idAtencion = value; }
        public string? Observaciones { get => observaciones; set => observaciones = value; }
        public TimeOnly? HoraInicio { get => horaInicio; set => horaInicio = value; }
        public TimeOnly? HoraFin { get => horaFin; set => horaFin = value; }
        public Cita? Cita { get => cita; set => cita = value; }
        public Receta? Receta { get => receta; set => receta = value; }
        public double PesoActual { get => pesoActual; set => pesoActual = value; }
        public string? Alergias { get => alergias; set => alergias = value; }
        public bool Activo { get => activo; set => activo = value; }
        public Mascota? Mascota { get => cita?.Mascota; }
    }
}
