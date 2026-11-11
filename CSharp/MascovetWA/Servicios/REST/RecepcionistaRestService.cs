using System.Net.Http.Json;
using System.Text.Json;
using System.Text.Json.Serialization;
using MascovetDomain.Usuarios;

namespace MascovetWA.Servicios.REST
{
    public class RecepcionistaRestService
    {
        private readonly HttpClient client;
        private static readonly JsonSerializerOptions opciones = new(JsonSerializerDefaults.Web)
        {
            Converters = { new JsonStringEnumConverter(), new FechaRestJsonConverter() },
            PreferredObjectCreationHandling = JsonObjectCreationHandling.Populate
        };

        public RecepcionistaRestService(HttpClient client)
        {
            this.client = client;
        }

        public async Task<List<Recepcionista>> ListarTodosAsync()
        {
            return await client.GetFromJsonAsync<List<Recepcionista>>("RecepcionistaRS/listarTodosRecepcionistas", opciones) ?? new List<Recepcionista>();
        }

        public async Task<Recepcionista?> BuscarPorIdAsync(int id)
        {
            return await client.GetFromJsonAsync<Recepcionista>($"RecepcionistaRS/buscarRecepcionistaPorId/{id}", opciones);
        }

        public async Task<int> InsertarAsync(Recepcionista entidad)
        {
            HttpResponseMessage response = await client.PostAsJsonAsync("RecepcionistaRS/insertarRecepcionista", entidad, opciones);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<int> ModificarAsync(Recepcionista entidad)
        {
            HttpResponseMessage response = await client.PutAsJsonAsync("RecepcionistaRS/modificarRecepcionista", entidad, opciones);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<int> EliminarAsync(int id)
        {
            HttpResponseMessage response = await client.DeleteAsync($"RecepcionistaRS/eliminarRecepcionista/{id}");
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }
    }
}
