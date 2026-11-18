using System.Net.Http.Json;
using System.Text.Json;
using System.Text.Json.Serialization;
using MascovetDomain.Citas;

namespace MascovetWA.Servicios.REST
{
    public class HorarioAtencionRestService
    {
        private readonly HttpClient client;
        private static readonly JsonSerializerOptions opciones = new(JsonSerializerDefaults.Web)
        {
            Converters = { new JsonStringEnumConverter(), new FechaRestJsonConverter() },
            PreferredObjectCreationHandling = JsonObjectCreationHandling.Populate
        };

        public HorarioAtencionRestService(HttpClient client)
        {
            this.client = client;
        }

        public async Task<List<HorarioAtencion>> ListarTodosAsync()
        {
            return await client.GetFromJsonAsync<List<HorarioAtencion>>("HorarioAtencionRS/listarTodosHorariosAtencion", opciones) ?? new List<HorarioAtencion>();
        }

        public async Task<HorarioAtencion?> BuscarPorIdAsync(int id)
        {
            return await client.GetFromJsonAsync<HorarioAtencion>($"HorarioAtencionRS/buscarHorarioAtencionPorId/{id}", opciones);
        }

        public async Task<int> InsertarAsync(HorarioAtencion entidad)
        {
            HttpResponseMessage response = await client.PostAsJsonAsync("HorarioAtencionRS/insertarHorarioAtencion", entidad, opciones);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<int> ModificarAsync(HorarioAtencion entidad)
        {
            HttpResponseMessage response = await client.PutAsJsonAsync("HorarioAtencionRS/modificarHorarioAtencion", entidad, opciones);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<int> EliminarAsync(int id)
        {
            HttpResponseMessage response = await client.DeleteAsync($"HorarioAtencionRS/eliminarHorarioAtencion/{id}");
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }
    }
}
