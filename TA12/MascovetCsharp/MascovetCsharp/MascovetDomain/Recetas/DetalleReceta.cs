using System;
using System.Collections.Generic;

namespace MascovetDomain.Recetas
{
    public class DetalleReceta
    {
        private int idDetalleReceta;
        private Receta? receta;
        private string? dosis;
        private string? frecuencia;
        private string? duracion;
        private double? montoTotal;
        private Medicamento? medicamento;
        private bool activo = true;

        public DetalleReceta() { }

        public int IdDetalleReceta { get => idDetalleReceta; set => idDetalleReceta = value; }
        public Receta? Receta { get => receta; set => receta = value; }
        public string? Dosis { get => dosis; set => dosis = value; }
        public string? Frecuencia { get => frecuencia; set => frecuencia = value; }
        public string? Duracion { get => duracion; set => duracion = value; }
        public double? MontoTotal { get => montoTotal; set => montoTotal = value; }
        public Medicamento? Medicamento { get => medicamento; set => medicamento = value; }
        public bool Activo { get => activo; set => activo = value; }
    }
}
