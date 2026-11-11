using System.Net.Http.Json;
using System.Text.Json;
using System.Text.Json.Serialization;
using MascovetDomain.Usuarios;

namespace MascovetWA.Servicios.REST
{
    public class ClienteRestService
    {
        private readonly HttpClient client;
        private static readonly JsonSerializerOptions opciones = new(JsonSerializerDefaults.Web)
        {
            Converters = { new JsonStringEnumConverter(), new FechaRestJsonConverter() },
            PreferredObjectCreationHandling = JsonObjectCreationHandling.Populate
        };

        public ClienteRestService(HttpClient client)
        {
            this.client = client;
        }

        public async Task<List<Cliente>> ListarTodosAsync()
        {
            return await client.GetFromJsonAsync<List<Cliente>>("ClienteRS/listarTodosClientes", opciones) ?? new List<Cliente>();
        }

        public async Task<Cliente?> BuscarPorIdAsync(int id)
        {
            return await client.GetFromJsonAsync<Cliente>($"ClienteRS/buscarClientePorId/{id}", opciones);
        }

        public async Task<int> InsertarAsync(Cliente entidad)
        {
            HttpResponseMessage response = await client.PostAsJsonAsync("ClienteRS/insertarCliente", entidad, opciones);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<int> ModificarAsync(Cliente entidad)
        {
            HttpResponseMessage response = await client.PutAsJsonAsync("ClienteRS/modificarCliente", entidad, opciones);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<int> EliminarAsync(int id)
        {
            HttpResponseMessage response = await client.DeleteAsync($"ClienteRS/eliminarCliente/{id}");
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }
    }
}
