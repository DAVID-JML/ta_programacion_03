using System.Net.Http.Json;
using System.Text.Json;
using System.Text.Json.Serialization;
using MascovetDomain.AtencionesMedicas;

namespace MascovetWA.Servicios.REST
{
    public class VacunaRestService
    {
        private readonly HttpClient client;
        private static readonly JsonSerializerOptions opciones = new(JsonSerializerDefaults.Web)
        {
            Converters = { new JsonStringEnumConverter(), new FechaRestJsonConverter() },
            PreferredObjectCreationHandling = JsonObjectCreationHandling.Populate
        };

        public VacunaRestService(HttpClient client)
        {
            this.client = client;
        }

        public async Task<List<Vacuna>> ListarTodosAsync()
        {
            return await client.GetFromJsonAsync<List<Vacuna>>("VacunaRS/listarTodosVacunas", opciones) ?? new List<Vacuna>();
        }

        public async Task<Vacuna?> BuscarPorIdAsync(int id)
        {
            return await client.GetFromJsonAsync<Vacuna>($"VacunaRS/buscarVacunaPorId/{id}", opciones);
        }

        public async Task<int> InsertarAsync(Vacuna entidad)
        {
            HttpResponseMessage response = await client.PostAsJsonAsync("VacunaRS/insertarVacuna", entidad, opciones);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<int> ModificarAsync(Vacuna entidad)
        {
            HttpResponseMessage response = await client.PutAsJsonAsync("VacunaRS/modificarVacuna", entidad, opciones);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<int> EliminarAsync(int id)
        {
            HttpResponseMessage response = await client.DeleteAsync($"VacunaRS/eliminarVacuna/{id}");
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }
    }
}
