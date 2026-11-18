using System.Net.Http.Json;
using System.Text.Json;
using System.Text.Json.Serialization;
using MascovetDomain.Recetas;

namespace MascovetWA.Servicios.REST
{
    public class DetalleRecetaRestService
    {
        private readonly HttpClient client;
        private static readonly JsonSerializerOptions opciones = new(JsonSerializerDefaults.Web)
        {
            Converters = { new JsonStringEnumConverter(), new FechaRestJsonConverter() },
            PreferredObjectCreationHandling = JsonObjectCreationHandling.Populate
        };

        public DetalleRecetaRestService(HttpClient client)
        {
            this.client = client;
        }

        public async Task<List<DetalleReceta>> ListarTodosAsync()
        {
            return await client.GetFromJsonAsync<List<DetalleReceta>>("DetalleRecetaRS/listarTodosDetallesReceta", opciones) ?? new List<DetalleReceta>();
        }

        public async Task<DetalleReceta?> BuscarPorIdAsync(int id)
        {
            return await client.GetFromJsonAsync<DetalleReceta>($"DetalleRecetaRS/buscarDetalleRecetaPorId/{id}", opciones);
        }

        public async Task<int> InsertarAsync(DetalleReceta entidad)
        {
            HttpResponseMessage response = await client.PostAsJsonAsync("DetalleRecetaRS/insertarDetalleReceta", entidad, opciones);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<int> ModificarAsync(DetalleReceta entidad)
        {
            HttpResponseMessage response = await client.PutAsJsonAsync("DetalleRecetaRS/modificarDetalleReceta", entidad, opciones);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<int> EliminarAsync(int id)
        {
            HttpResponseMessage response = await client.DeleteAsync($"DetalleRecetaRS/eliminarDetalleReceta/{id}");
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }
    }
}
