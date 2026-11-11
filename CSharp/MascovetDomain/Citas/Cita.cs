using System;
using System.Collections.Generic;
using MascovetDomain.AtencionesMedicas;
using MascovetDomain.Enums;
using MascovetDomain.Mascotas;
using MascovetDomain.Usuarios;

namespace MascovetDomain.Citas
{
    public class Cita
    {
        private int idCita;
        private Mascota? mascota;
        private DateTime? fecha;
        private TimeOnly? hora;
        private EstadoCita? estado = EstadoCita.RESERVADA;
        private TipoAtencion? tipoAtencion;
        private string? motivoCancelacion;
        private bool activo = true;
        private Recepcionista? recepcionista;
        private Veterinario? veterinario;
        private readonly List<AtencionMedica> atencionesMedicas = new List<AtencionMedica>();

        public Cita() { }

        public int IdCita { get => idCita; set => idCita = value; }
        public Mascota? Mascota { get => mascota; set => mascota = value; }
        public DateTime? Fecha { get => fecha; set => fecha = value; }
        public TimeOnly? Hora { get => hora; set => hora = value; }
        public EstadoCita? Estado { get => estado; set => estado = value; }
        public TipoAtencion? TipoAtencion { get => tipoAtencion; set => tipoAtencion = value; }
        public string? MotivoCancelacion { get => motivoCancelacion; set => motivoCancelacion = value; }
        public bool Activo { get => activo; set => activo = value; }
        public Recepcionista? Recepcionista { get => recepcionista; set => recepcionista = value; }
        public Veterinario? Veterinario { get => veterinario; set => veterinario = value; }
        public List<AtencionMedica> AtencionesMedicas { get => atencionesMedicas; }
        public Cliente? Cliente { get => mascota?.Cliente; }

        public void Confirmar() { estado = EstadoCita.CONFIRMADA; }
        public void Cancelar() { estado = EstadoCita.CANCELADA; }
        public void AgregarAtencionMedica(AtencionMedica atencionMedica) { atencionesMedicas.Add(atencionMedica); }
    }
}
