using System;
using System.Collections.Generic;
using MascovetDomain.AtencionesMedicas;

namespace MascovetDomain.Recetas
{
    public class Receta
    {
        private int idReceta;
        private AtencionMedica? atencionMedica;
        private DateTime? fecha;
        private string? indicaciones;
        private bool activo = true;
        private readonly List<DetalleReceta> detallesReceta = new List<DetalleReceta>();

        public Receta() { }

        public int IdReceta { get => idReceta; set => idReceta = value; }
        public AtencionMedica? AtencionMedica { get => atencionMedica; set => atencionMedica = value; }
        public DateTime? Fecha { get => fecha; set => fecha = value; }
        public string? Indicaciones { get => indicaciones; set => indicaciones = value; }
        public bool Activo { get => activo; set => activo = value; }
        public List<DetalleReceta> DetallesReceta { get => detallesReceta; }

        public void AgregarDetalleReceta(DetalleReceta detalleReceta) { detallesReceta.Add(detalleReceta); }
    }
}
