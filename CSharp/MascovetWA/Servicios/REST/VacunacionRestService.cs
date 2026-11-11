using System.Net.Http.Json;
using System.Text.Json;
using System.Text.Json.Serialization;
using MascovetDomain.AtencionesMedicas;

namespace MascovetWA.Servicios.REST
{
    public class VacunacionRestService
    {
        private readonly HttpClient client;
        private static readonly JsonSerializerOptions opciones = new(JsonSerializerDefaults.Web)
        {
            Converters = { new JsonStringEnumConverter(), new FechaRestJsonConverter() },
            PreferredObjectCreationHandling = JsonObjectCreationHandling.Populate
        };

        public VacunacionRestService(HttpClient client)
        {
            this.client = client;
        }

        public async Task<List<Vacunacion>> ListarTodosAsync()
        {
            return await client.GetFromJsonAsync<List<Vacunacion>>("VacunacionRS/listarTodosVacunaciones", opciones) ?? new List<Vacunacion>();
        }

        public async Task<Vacunacion?> BuscarPorIdAsync(int id)
        {
            return await client.GetFromJsonAsync<Vacunacion>($"VacunacionRS/buscarVacunacionPorId/{id}", opciones);
        }

        public async Task<int> InsertarAsync(Vacunacion entidad)
        {
            HttpResponseMessage response = await client.PostAsJsonAsync("VacunacionRS/insertarVacunacion", entidad, opciones);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<int> ModificarAsync(Vacunacion entidad)
        {
            HttpResponseMessage response = await client.PutAsJsonAsync("VacunacionRS/modificarVacunacion", entidad, opciones);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<int> EliminarAsync(int id)
        {
            HttpResponseMessage response = await client.DeleteAsync($"VacunacionRS/eliminarVacunacion/{id}");
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<int> InsertarConVacunasAsync(Vacunacion vacunacion)
        {
            HttpResponseMessage response = await client.PostAsJsonAsync("VacunacionRS/insertarConVacunas", vacunacion, opciones);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }
    }
}
