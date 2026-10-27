using MascovetWA.Components.Layout;

namespace MascovetWA.Components.Layout;

/// <summary>Sesión del usuario en este circuito. Provisional hasta usar autenticación real.</summary>
public sealed class UserContext
{
        public UserRole Role { get; private set; } = UserRole.Cliente;
    public string Nombre { get; private set; } = string.Empty;
    public bool IsAuthenticated { get; private set; }
    public event Action? Changed;

    public void SignIn(UserRole role, string nombre)
    {
        Role = role;
        Nombre = nombre;
        IsAuthenticated = true;
        Changed?.Invoke();
    }

    public void SignOut()
    {
        IsAuthenticated = false;
        Nombre = string.Empty;
        Changed?.Invoke();
    }

    public void SetRole(UserRole role) // solo para el selector de pruebas
    {
        Role = role;
        Changed?.Invoke();
    }
}