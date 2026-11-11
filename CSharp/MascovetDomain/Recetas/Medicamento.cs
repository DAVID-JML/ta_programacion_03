using System;
using System.Collections.Generic;

namespace MascovetDomain.Recetas
{
    public class Medicamento
    {
        private int idMedicamento;
        private string? nombre;
        private string? descripcion;
        private DateTime? fechaVencimiento;
        private bool activo = true;
        private double monto;

        public Medicamento() { }

        public int IdMedicamento { get => idMedicamento; set => idMedicamento = value; }
        public string? Nombre { get => nombre; set => nombre = value; }
        public string? Descripcion { get => descripcion; set => descripcion = value; }
        public DateTime? FechaVencimiento { get => fechaVencimiento; set => fechaVencimiento = value; }
        public bool Activo { get => activo; set => activo = value; }
        public double Monto { get => monto; set => monto = value; }
    }
}
