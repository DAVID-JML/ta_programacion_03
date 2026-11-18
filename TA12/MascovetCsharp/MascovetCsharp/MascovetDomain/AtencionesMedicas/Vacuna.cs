using System;
using System.Collections.Generic;

namespace MascovetDomain.AtencionesMedicas
{
    public class Vacuna
    {
        private int idVacuna;
        private string? nombre;
        private string? descripcion;
        private bool activo = true;

        public Vacuna() { }

        public int IdVacuna { get => idVacuna; set => idVacuna = value; }
        public string? Nombre { get => nombre; set => nombre = value; }
        public string? Descripcion { get => descripcion; set => descripcion = value; }
        public bool Activo { get => activo; set => activo = value; }
    }
}
