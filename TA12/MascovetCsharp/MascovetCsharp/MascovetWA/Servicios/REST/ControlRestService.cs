using System.Net.Http.Json;
using System.Text.Json;
using System.Text.Json.Serialization;
using MascovetDomain.AtencionesMedicas;

namespace MascovetWA.Servicios.REST
{
    public class ControlRestService
    {
        private readonly HttpClient client;
        private static readonly JsonSerializerOptions opciones = new(JsonSerializerDefaults.Web)
        {
            Converters = { new JsonStringEnumConverter(), new FechaRestJsonConverter() },
            PreferredObjectCreationHandling = JsonObjectCreationHandling.Populate
        };

        public ControlRestService(HttpClient client)
        {
            this.client = client;
        }

        public async Task<List<Control>> ListarTodosAsync()
        {
            return await client.GetFromJsonAsync<List<Control>>("ControlRS/listarTodosControles", opciones) ?? new List<Control>();
        }

        public async Task<Control?> BuscarPorIdAsync(int id)
        {
            return await client.GetFromJsonAsync<Control>($"ControlRS/buscarControlPorId/{id}", opciones);
        }

        public async Task<int> InsertarAsync(Control entidad)
        {
            HttpResponseMessage response = await client.PostAsJsonAsync("ControlRS/insertarControl", entidad, opciones);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<int> ModificarAsync(Control entidad)
        {
            HttpResponseMessage response = await client.PutAsJsonAsync("ControlRS/modificarControl", entidad, opciones);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<int> EliminarAsync(int id)
        {
            HttpResponseMessage response = await client.DeleteAsync($"ControlRS/eliminarControl/{id}");
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }
    }
}
