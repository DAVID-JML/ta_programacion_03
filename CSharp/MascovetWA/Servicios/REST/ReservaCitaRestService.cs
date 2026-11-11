using System.Globalization;
using System.Net.Http.Json;
using System.Text.Json;
using System.Text.Json.Serialization;
using MascovetDomain.Citas;

namespace MascovetWA.Servicios.REST
{
    public class ResultadoReserva
    {
        public bool Exito { get; set; }
        public int IdCita { get; set; }
        public string? Mensaje { get; set; }
    }

    public class ReservaCitaRestService
    {
        private readonly HttpClient client;
        private static readonly JsonSerializerOptions opciones = new(JsonSerializerDefaults.Web)
        {
            Converters = { new JsonStringEnumConverter(), new FechaRestJsonConverter() },
            PreferredObjectCreationHandling = JsonObjectCreationHandling.Populate
        };

        public ReservaCitaRestService(HttpClient client)
        {
            this.client = client;
        }

        public async Task<List<HorarioAtencion>> ListarDisponiblesAsync(DateOnly fecha, int idVeterinario = 0, string? especialidad = null)
        {
            string url = "DisponibilidadRS/listarDisponibles?fecha=" + fecha.ToString("yyyy-MM-dd", CultureInfo.InvariantCulture)
                + "&idVeterinario=" + idVeterinario;
            if (!string.IsNullOrWhiteSpace(especialidad)) url += "&especialidad=" + Uri.EscapeDataString(especialidad);
            return await client.GetFromJsonAsync<List<HorarioAtencion>>(url, opciones) ?? new List<HorarioAtencion>();
        }

        public async Task<ResultadoReserva> ReservarAsync(Cita cita)
        {
            HttpResponseMessage response = await client.PostAsJsonAsync("CitaRS/reservarCita", cita, opciones);
            if (!response.IsSuccessStatusCode) return new ResultadoReserva { Exito = false };
            int id = await response.Content.ReadFromJsonAsync<int>();
            return new ResultadoReserva { Exito = id > 0, IdCita = id };
        }
    }
}
