using MascovetDomain.Enums;

namespace MascovetWA.Servicios.Validaciones;

public static class ReglasCitas
{
    public static bool Permite(Especialidad? especialidad, TipoAtencion? tipo) =>
        especialidad.HasValue && tipo.HasValue && (especialidad.Value switch
        {
            Especialidad.MEDICINA_GENERAL => tipo.Value is TipoAtencion.CONSULTA or TipoAtencion.CONTROL or TipoAtencion.VACUNACION,
            Especialidad.CIRUGIA => tipo.Value is TipoAtencion.CONSULTA or TipoAtencion.CONTROL or TipoAtencion.CIRUGIA,
            Especialidad.DERMATOLOGIA or Especialidad.CARDIOLOGIA or Especialidad.ODONTOLOGIA =>
                tipo.Value is TipoAtencion.CONSULTA or TipoAtencion.CONTROL,
            _ => false
        });

    public static int Prioridad(EstadoCita? estado) => estado switch
    {
        EstadoCita.CONFIRMADA => 0,
        EstadoCita.RESERVADA => 1,
        EstadoCita.ATENDIDA => 2,
        EstadoCita.CANCELADA => 3,
        _ => 4
    };

    // DATE de MySQL: normalizar la fecha al mediodía para evitar desplazamientos
    // de día al serializar/deserializar java.util.Date entre Java y C#.
    public static DateTime FechaParaRest(DateTime fecha) =>
        DateTime.SpecifyKind(fecha.Date.AddHours(12), DateTimeKind.Unspecified);
}
