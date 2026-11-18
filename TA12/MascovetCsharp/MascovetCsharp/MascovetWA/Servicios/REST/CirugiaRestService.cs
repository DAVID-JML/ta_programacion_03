using System.Net.Http.Json;
using System.Text.Json;
using System.Text.Json.Serialization;
using MascovetDomain.AtencionesMedicas;

namespace MascovetWA.Servicios.REST
{
    public class CirugiaRestService
    {
        private readonly HttpClient client;
        private static readonly JsonSerializerOptions opciones = new(JsonSerializerDefaults.Web)
        {
            Converters = { new JsonStringEnumConverter(), new FechaRestJsonConverter() },
            PreferredObjectCreationHandling = JsonObjectCreationHandling.Populate
        };

        public CirugiaRestService(HttpClient client)
        {
            this.client = client;
        }

        public async Task<List<Cirugia>> ListarTodosAsync()
        {
            return await client.GetFromJsonAsync<List<Cirugia>>("CirugiaRS/listarTodosCirugias", opciones) ?? new List<Cirugia>();
        }

        public async Task<Cirugia?> BuscarPorIdAsync(int id)
        {
            return await client.GetFromJsonAsync<Cirugia>($"CirugiaRS/buscarCirugiaPorId/{id}", opciones);
        }

        public async Task<int> InsertarAsync(Cirugia entidad)
        {
            HttpResponseMessage response = await client.PostAsJsonAsync("CirugiaRS/insertarCirugia", entidad, opciones);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<int> ModificarAsync(Cirugia entidad)
        {
            HttpResponseMessage response = await client.PutAsJsonAsync("CirugiaRS/modificarCirugia", entidad, opciones);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<int> EliminarAsync(int id)
        {
            HttpResponseMessage response = await client.DeleteAsync($"CirugiaRS/eliminarCirugia/{id}");
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }
    }
}
