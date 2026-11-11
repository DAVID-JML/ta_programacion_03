using MascovetWA.Components.Layout;

namespace MascovetWA.Components.Layout;

/// <summary>Sesión del usuario en este circuito. Provisional hasta usar autenticación real.</summary>
public sealed class UserContext
{
    public UserRole Role { get; private set; } = UserRole.Cliente;
    public string Nombre { get; private set; } = string.Empty;
    // Vinculación temporal por nombre_usuario del SQL de prueba; no sustituye autenticación.
    public string? UsuarioVinculado { get; private set; }
    public bool IsAuthenticated { get; private set; }
    public event Action? Changed;

    public void SignIn(UserRole role, string nombre, string? usuarioVinculado = null)
    {
        Role = role;
        Nombre = nombre;
        UsuarioVinculado = usuarioVinculado;
        IsAuthenticated = true;
        Changed?.Invoke();
    }

    public void SignOut()
    {
        IsAuthenticated = false;
        Nombre = string.Empty;
        UsuarioVinculado = null;
        Changed?.Invoke();
    }

    public void SetRole(UserRole role) // solo para el selector de pruebas
    {
        Role = role;
        Changed?.Invoke();
    }
}