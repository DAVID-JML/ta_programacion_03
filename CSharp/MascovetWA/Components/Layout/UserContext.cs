namespace MascovetWA.Components.Layout;

/// <summary>
/// Sesión del usuario en este circuito.
/// Provisional hasta usar autenticación real.
/// </summary>
public sealed class UserContext
{
    public UserRole Role { get; private set; } = UserRole.Cliente;

    public string Nombre { get; private set; } = string.Empty;

    // Vinculación temporal por nombre_usuario del SQL de prueba.
    public string? UsuarioVinculado { get; private set; }

    // Identificador del usuario en la base de datos.
    public int? IdUsuario { get; private set; }

    public bool IsAuthenticated { get; private set; }

    public event Action? Changed;

    public void SignIn(
        UserRole role,
        string nombre,
        string? usuarioVinculado = null,
        int? idUsuario = null)
    {
        Role = role;
        Nombre = nombre;
        UsuarioVinculado = usuarioVinculado;
        IdUsuario = idUsuario;
        IsAuthenticated = true;

        Changed?.Invoke();
    }

    public void SignOut()
    {
        IsAuthenticated = false;
        Nombre = string.Empty;
        UsuarioVinculado = null;
        IdUsuario = null;

        Changed?.Invoke();
    }

    public void SetRole(UserRole role)
    {
        Role = role;
        Changed?.Invoke();
    }
}