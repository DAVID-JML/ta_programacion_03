using System.Net.Http.Json;
using System.Net;
using System.Text.Json;
using MascovetDomain.Usuarios;

namespace MascovetWA.Servicios.REST
{
    public class CuentaUsuarioRestService
    {
        private readonly HttpClient client;
        public CuentaUsuarioRestService(HttpClient client) { this.client = client; }

        public bool Verificar(CuentaUsuario cuentaUsuario) =>
            VerificarAsync(cuentaUsuario).GetAwaiter().GetResult();

        public async Task<bool> VerificarAsync(CuentaUsuario cuentaUsuario)
        {
            using var response = await client.PostAsJsonAsync("CuentaUsuarioRS/verificar", cuentaUsuario);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<bool>();
        }

        public async Task<CuentaUsuario?> AutenticarAsync(CuentaUsuario cuentaUsuario)
        {
            using var response = await client.PostAsJsonAsync("CuentaUsuarioRS/autenticar", cuentaUsuario);

             
             
            if (response.StatusCode == HttpStatusCode.NoContent)
                return null;

            response.EnsureSuccessStatusCode();
            string contenido = await response.Content.ReadAsStringAsync();
            if (string.IsNullOrWhiteSpace(contenido) || contenido.Trim() == "null")
                return null;

            return JsonSerializer.Deserialize<CuentaUsuario>(contenido,
                new JsonSerializerOptions(JsonSerializerDefaults.Web));
        }

        public async Task<int> CambiarContrasenaAsync(CuentaUsuario cuentaUsuario)
        {
            using var response = await client.PostAsJsonAsync("CuentaUsuarioRS/cambiarContrasena", cuentaUsuario);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<List<CuentaUsuario>> ListarTodosAsync() =>
            await client.GetFromJsonAsync<List<CuentaUsuario>>("CuentaUsuarioRS/listarTodos") ?? new();

        public async Task<int> InsertarAsync(CuentaUsuario cuentaUsuario)
        {
            using var response = await client.PostAsJsonAsync("CuentaUsuarioRS/insertarCuenta", cuentaUsuario);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<int> ModificarAsync(CuentaUsuario cuentaUsuario)
        {
            using var response = await client.PutAsJsonAsync("CuentaUsuarioRS/modificarCuenta", cuentaUsuario);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }
    }
}
