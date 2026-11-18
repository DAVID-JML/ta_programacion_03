using System.Security.Claims;
namespace MascovetWA.Components.Layout;

 
public sealed class UserContext
{
    public UserRole Role { get; private set; } = UserRole.Cliente;
    public string Nombre { get; private set; } = string.Empty;
    public string? UsuarioVinculado { get; private set; }
    public int IdUsuario { get; private set; }
    public bool IsAuthenticated { get; private set; }
    public event Action? Changed;

    public void RestoreFromClaims(ClaimsPrincipal principal)
    {
        if (principal.Identity?.IsAuthenticated != true ||
            !Enum.TryParse(principal.FindFirst(ClaimTypes.Role)?.Value, out UserRole rol))
        {
            SignOut();
            return;
        }
        Role = rol;
        Nombre = principal.FindFirst(ClaimTypes.Name)?.Value ?? "Usuario";
        UsuarioVinculado = principal.FindFirst("usuarioVinculado")?.Value;
        int.TryParse(principal.FindFirst(ClaimTypes.NameIdentifier)?.Value, out int id);
        IdUsuario = id;
        IsAuthenticated = true;
        Changed?.Invoke();
    }

    public void SignOut()
    {
        IsAuthenticated = false;
        Nombre = string.Empty;
        UsuarioVinculado = null;
        IdUsuario = 0;
        Changed?.Invoke();
    }
}
