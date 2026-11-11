using System.Net.Http.Json;
using System.Text.Json;
using System.Text.Json.Serialization;
using MascovetDomain.Mascotas;

namespace MascovetWA.Servicios.REST
{
    public class MascotaRestService
    {
        private readonly HttpClient client;
        private static readonly JsonSerializerOptions opciones = new(JsonSerializerDefaults.Web)
        {
            Converters = { new JsonStringEnumConverter(), new FechaRestJsonConverter() },
            PreferredObjectCreationHandling = JsonObjectCreationHandling.Populate
        };

        public MascotaRestService(HttpClient client)
        {
            this.client = client;
        }

        public async Task<List<Mascota>> ListarTodosAsync()
        {
            return await client.GetFromJsonAsync<List<Mascota>>("MascotaRS/listarTodosMascotas", opciones) ?? new List<Mascota>();
        }

        public async Task<Mascota?> BuscarPorIdAsync(int id)
        {
            return await client.GetFromJsonAsync<Mascota>($"MascotaRS/buscarMascotaPorId/{id}", opciones);
        }

        public async Task<int> InsertarAsync(Mascota entidad)
        {
            HttpResponseMessage response = await client.PostAsJsonAsync("MascotaRS/insertarMascota", entidad, opciones);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<int> ModificarAsync(Mascota entidad)
        {
            HttpResponseMessage response = await client.PutAsJsonAsync("MascotaRS/modificarMascota", entidad, opciones);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<int> EliminarAsync(int id)
        {
            HttpResponseMessage response = await client.DeleteAsync($"MascotaRS/eliminarMascota/{id}");
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }
    }
}
