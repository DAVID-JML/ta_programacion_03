using MascovetWA.Components.Layout;
using Microsoft.AspNetCore.Identity;

namespace MascovetWA.Components.Layout;

public sealed record AuthResult(UserRole Role, string Nombre, string? UsuarioVinculado);

/// <summary>TEMPORAL: usuarios de prueba en memoria. Reemplazar por acceso a base de datos.</summary>
public sealed class AuthService
{
    private sealed record DemoUser(string Login, string Hash, UserRole Role, string Nombre, string? UsuarioVinculado);

    private readonly PasswordHasher<string> _hasher = new();
    private readonly List<DemoUser> _users = new();

    public AuthService()
    {
        // La vinculación temporal usa nombre_usuario del SQL de prueba, no el nombre ni correo editable.
        Add("ana@correo.com", "Cliente123!", UserRole.Cliente, "Ana", "prueba_cliente_ana");
        Add("recepcion", "Recep123!", UserRole.Recepcionista, "Recepción");
        // La cuenta antigua se conserva como alias de prueba para el veterinario de los INSERT.
        Add("vet.paredes", "Vet123!", UserRole.Veterinario, "Luis Salazar", "prueba_veterinario_luis");
        Add("vet.salazar", "Vet123!", UserRole.Veterinario, "Luis Salazar", "prueba_veterinario_luis");
        Add("admin", "Admin123!", UserRole.Administrador, "Administrador");
    }

    private void Add(string login, string password, UserRole role, string nombre, string? usuarioVinculado = null) =>
        _users.Add(new DemoUser(login, _hasher.HashPassword(login, password), role, nombre, usuarioVinculado));

    /// <summary>Devuelve null si las credenciales no son válidas (sin indicar cuál falló).</summary>
    public AuthResult? Validar(string login, string password)
    {
        login = (login ?? string.Empty).Trim();
        var user = _users.FirstOrDefault(u => string.Equals(u.Login, login, StringComparison.OrdinalIgnoreCase));
        if (user is null) return null;

        // Los clientes ingresan con correo; el personal usa su cuenta asignada.
        var esCorreo = login.Contains('@');
        if (esCorreo != (user.Role == UserRole.Cliente)) return null;

        var result = _hasher.VerifyHashedPassword(user.Login, user.Hash, password ?? string.Empty);
        return result == PasswordVerificationResult.Failed ? null : new AuthResult(user.Role, user.Nombre, user.UsuarioVinculado);
    }
}