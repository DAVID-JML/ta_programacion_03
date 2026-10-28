namespace MascovetWA.Components.Layout;

public enum UserRole { Administrador, Recepcionista, Veterinario, Cliente }

/// <param name="Roles">Roles que ven la entrada. null = todos.</param>
public sealed record NavItem(string Text, string Href, string Icon, UserRole[]? Roles = null, bool Bottom = false);
public static class NavCatalog
{
    private static readonly UserRole[] Admin = { UserRole.Administrador };
    private static readonly UserRole[] Recep = { UserRole.Recepcionista };
    private static readonly UserRole[] Vet = { UserRole.Veterinario };
    private static readonly UserRole[] Cli = { UserRole.Cliente };

    public static readonly NavItem[] Items =
    {
        // Cliente (RF012-016, RF018, RF022)
        new("Inicio", "cliente", "home", Cli),
        new("Mis mascotas", "cliente/mascotas", "heart", Cli),
        new("Reservar cita", "cliente/reservar", "plus", Cli),
        new("Mis citas", "cliente/citas", "calendar", Cli),
        new("Mi perfil", "cliente/perfil", "user", Cli, true),
       
        // Recepcionista (RF002, RF012-019)
        new("Inicio", "recepcion", "home", Recep),
        new("Clientes", "recepcion/clientes", "users", Recep),
        new("Mascotas", "recepcion/mascotas", "heart", Recep),
        new("Citas", "recepcion/citas", "calendar", Recep),

        // Veterinario (RF021-026, RF029)
        new("Inicio", "veterinario", "home", Vet),
        new("Mi agenda", "veterinario/agenda", "calendar", Vet),
        new("Registrar atención", "veterinario/atencion", "file-text", Vet),   // <- nuevo
        new("Historias clínicas", "veterinario/historias", "file-text", Vet),
        new("Vacunación", "veterinario/vacunas", "check-circle", Vet),
        new("Recetas", "veterinario/recetas", "file-text", Vet),

        // Administrador (RF001, RF006-011, RF027-029)
        new("Inicio", "administrador", "home", Admin),
        new("Personal", "administrador/personal", "users", Admin),
        new("Horarios", "administrador/horarios", "clock", Admin),
        new("Servicios", "administrador/servicios", "grid", Admin),
        new("Medicamentos", "administrador/medicamentos", "file-text", Admin),
        new("Reportes", "administrador/reportes", "bar-chart", Admin)
    };

    public static IEnumerable<NavItem> For(UserRole role) =>
        Items.Where(i => i.Roles is null || i.Roles.Contains(role));

    public static string HomeFor(UserRole role) => For(role).First().Href;
}
