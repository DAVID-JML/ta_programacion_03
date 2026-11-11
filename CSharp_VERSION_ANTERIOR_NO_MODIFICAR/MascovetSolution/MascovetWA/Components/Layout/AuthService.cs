using MascovetWA.Components.Layout;
using Microsoft.AspNetCore.Identity;

namespace MascovetWA.Components.Layout;

public sealed record AuthResult(UserRole Role, string Nombre);

/// <summary>TEMPORAL: usuarios de prueba en memoria. Reemplazar por acceso a base de datos.</summary>
public sealed class AuthService
{
    private sealed record DemoUser(string Login, string Hash, UserRole Role, string Nombre);

    private readonly PasswordHasher<string> _hasher = new();
    private readonly List<DemoUser> _users = new();

    public AuthService()
    {
        Add("ana@correo.com", "Cliente123!", UserRole.Cliente, "Ana");
        Add("recepcion", "Recep123!", UserRole.Recepcionista, "Recepción");
        Add("vet.paredes", "Vet123!", UserRole.Veterinario, "Dra. Paredes");
        Add("admin", "Admin123!", UserRole.Administrador, "Administrador");
    }

    private void Add(string login, string password, UserRole role, string nombre) =>
        _users.Add(new DemoUser(login, _hasher.HashPassword(login, password), role, nombre));

    /// <summary>Devuelve null si las credenciales no son válidas (sin indicar cuál falló).</summary>
    public AuthResult? Validar(string login, string password)
    {
        login = (login ?? string.Empty).Trim();
        var user = _users.FirstOrDefault(u => string.Equals(u.Login, login, StringComparison.OrdinalIgnoreCase));
        if (user is null) return null;

        // RF003: el Cliente entra con correo; el personal, con usuario.
        var esCorreo = login.Contains('@');
        if (esCorreo != (user.Role == UserRole.Cliente)) return null;

        var result = _hasher.VerifyHashedPassword(user.Login, user.Hash, password ?? string.Empty);
        return result == PasswordVerificationResult.Failed ? null : new AuthResult(user.Role, user.Nombre);
    }
}