using System.Net.Http.Json;
using System.Text.Json;
using System.Text.Json.Serialization;
using MascovetDomain.Usuarios;

namespace MascovetWA.Servicios.REST
{
    public class AdministradorRestService
    {
        private readonly HttpClient client;
        private static readonly JsonSerializerOptions opciones = new(JsonSerializerDefaults.Web)
        {
            Converters = { new JsonStringEnumConverter(), new FechaRestJsonConverter() },
            PreferredObjectCreationHandling = JsonObjectCreationHandling.Populate
        };

        public AdministradorRestService(HttpClient client)
        {
            this.client = client;
        }

        public async Task<List<Administrador>> ListarTodosAsync()
        {
            return await client.GetFromJsonAsync<List<Administrador>>("AdministradorRS/listarTodosAdministradores", opciones) ?? new List<Administrador>();
        }

        public async Task<Administrador?> BuscarPorIdAsync(int id)
        {
            return await client.GetFromJsonAsync<Administrador>($"AdministradorRS/buscarAdministradorPorId/{id}", opciones);
        }

        public async Task<int> InsertarAsync(Administrador entidad)
        {
            HttpResponseMessage response = await client.PostAsJsonAsync("AdministradorRS/insertarAdministrador", entidad, opciones);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<int> ModificarAsync(Administrador entidad)
        {
            HttpResponseMessage response = await client.PutAsJsonAsync("AdministradorRS/modificarAdministrador", entidad, opciones);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<int> EliminarAsync(int id)
        {
            HttpResponseMessage response = await client.DeleteAsync($"AdministradorRS/eliminarAdministrador/{id}");
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }
    }
}
