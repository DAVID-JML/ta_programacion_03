using System;
using System.Collections.Generic;
using MascovetDomain.Enums;
using MascovetDomain.Usuarios;

namespace MascovetDomain.Citas
{
    public class HorarioAtencion
    {
        private int idHorario;
        private Veterinario? veterinario;
        private Dia? dia;
        private TimeOnly? horaInicio;
        private TimeOnly? horaFin;
        private bool activo = true;

        public HorarioAtencion() { }

        public HorarioAtencion(Dia? dia, TimeOnly? horaInicio, TimeOnly? horaFin)
        {
            this.dia = dia;
            this.horaInicio = horaInicio;
            this.horaFin = horaFin;
        }

        public int IdHorario { get => idHorario; set => idHorario = value; }
        public Veterinario? Veterinario { get => veterinario; set => veterinario = value; }
        public Dia? Dia { get => dia; set => dia = value; }
        public TimeOnly? HoraInicio { get => horaInicio; set => horaInicio = value; }
        public TimeOnly? HoraFin { get => horaFin; set => horaFin = value; }
        public bool Activo { get => activo; set => activo = value; }
    }
}
