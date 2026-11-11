using System.Net;
using System.Net.Http.Json;

namespace MascovetWA.Servicios.REST;

public sealed class AuthRestService
{
    private readonly HttpClient _client;

    public AuthRestService(HttpClient client)
    {
        _client = client;
    }

    public async Task<SesionLogin?> AutenticarAsync(
        string correo, string contrasena)
    {
        using var respuesta = await _client.PostAsJsonAsync(
            "UsuarioRS/autenticar",
            new { correo, contrasena }
        );

        if (respuesta.StatusCode == HttpStatusCode.Unauthorized)
        {
            return null;
        }

        respuesta.EnsureSuccessStatusCode();

        return await respuesta.Content.ReadFromJsonAsync<SesionLogin>()
            ?? throw new InvalidOperationException("La respuesta de login está vacía.");
    }

    public async Task<SesionLogin?> AutenticarPersonalAsync(
        string nombreUsuario, string contrasena)
    {
        using var respuesta = await _client.PostAsJsonAsync(
            "UsuarioRS/autenticarPersonal",
            new { nombreUsuario, contrasena }
        );

        if (respuesta.StatusCode == HttpStatusCode.Unauthorized)
        {
            return null;
        }

        respuesta.EnsureSuccessStatusCode();

        return await respuesta.Content.ReadFromJsonAsync<SesionLogin>()
            ?? throw new InvalidOperationException("La respuesta de login está vacía.");
    }

    public sealed record SesionLogin(
        int IdUsuario,
        string Nombre,
        string NombreUsuario,
        string Rol
    );
}