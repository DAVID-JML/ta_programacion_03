using MascovetDomain.Citas;
using MascovetDomain.Enums;

namespace MascovetWA.Servicios.Validaciones;

 
 
public static class DisponibilidadCitas
{
    public static List<TimeOnly> ObtenerHoras(DateOnly fecha, int idVeterinario,
        IEnumerable<HorarioAtencion> horarios, IEnumerable<Cita> citas)
    {
        if (idVeterinario <= 0 || fecha < DateOnly.FromDateTime(DateTime.Today))
            return new List<TimeOnly>();

        int dia = ((int)fecha.DayOfWeek + 6) % 7;  
        var horas = new List<TimeOnly>();

        foreach (var horario in horarios)
        {
            if (!horario.Activo || horario.Veterinario?.IdUsuario != idVeterinario ||
                !horario.Dia.HasValue || (int)horario.Dia.Value != dia ||
                !horario.HoraInicio.HasValue || !horario.HoraFin.HasValue)
                continue;

            TimeSpan actual = horario.HoraInicio.Value.ToTimeSpan();
            TimeSpan fin = horario.HoraFin.Value.ToTimeSpan();
            while (actual + TimeSpan.FromMinutes(30) <= fin)
            {
                TimeOnly inicio = TimeOnly.FromTimeSpan(actual);
                if (fecha.ToDateTime(inicio) > DateTime.Now &&
                    !citas.Any(c => c.Activo && c.Veterinario?.IdUsuario == idVeterinario &&
                        c.Estado is not (EstadoCita.CANCELADA or EstadoCita.ATENDIDA) &&
                        c.Fecha.HasValue && DateOnly.FromDateTime(c.Fecha.Value) == fecha &&
                        c.Hora.HasValue &&
                        c.Hora.Value < inicio.AddMinutes(30) &&
                        inicio < c.Hora.Value.AddMinutes(30)))
                    horas.Add(inicio);
                actual += TimeSpan.FromMinutes(30);
            }
        }
        return horas.Distinct().OrderBy(h => h).ToList();
    }
}
