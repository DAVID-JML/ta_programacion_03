using System.Net.Http.Json;
using System.Text.Json;
using System.Text.Json.Serialization;
using MascovetDomain.Usuarios;

namespace MascovetWA.Servicios.REST
{
    public class VeterinarioRestService
    {
        private readonly HttpClient client;
        private static readonly JsonSerializerOptions opciones = new(JsonSerializerDefaults.Web)
        {
            Converters = { new JsonStringEnumConverter(), new FechaRestJsonConverter() },
            PreferredObjectCreationHandling = JsonObjectCreationHandling.Populate
        };

        public VeterinarioRestService(HttpClient client)
        {
            this.client = client;
        }

        public async Task<List<Veterinario>> ListarTodosAsync()
        {
            return await client.GetFromJsonAsync<List<Veterinario>>("VeterinarioRS/listarTodosVeterinarios", opciones) ?? new List<Veterinario>();
        }

        public async Task<Veterinario?> BuscarPorIdAsync(int id)
        {
            return await client.GetFromJsonAsync<Veterinario>($"VeterinarioRS/buscarVeterinarioPorId/{id}", opciones);
        }

        public async Task<int> InsertarAsync(Veterinario entidad)
        {
            HttpResponseMessage response = await client.PostAsJsonAsync("VeterinarioRS/insertarVeterinario", entidad, opciones);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<int> ModificarAsync(Veterinario entidad)
        {
            HttpResponseMessage response = await client.PutAsJsonAsync("VeterinarioRS/modificarVeterinario", entidad, opciones);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<int> EliminarAsync(int id)
        {
            HttpResponseMessage response = await client.DeleteAsync($"VeterinarioRS/eliminarVeterinario/{id}");
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<int> InsertarConHorariosAsync(Veterinario veterinario)
        {
            HttpResponseMessage response = await client.PostAsJsonAsync("VeterinarioRS/insertarConHorarios", veterinario, opciones);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }
    }
}
