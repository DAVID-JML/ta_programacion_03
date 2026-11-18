using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authentication.Cookies;
using Microsoft.AspNetCore.Antiforgery;
using Microsoft.AspNetCore.Components.Authorization;
using Microsoft.AspNetCore.Authorization;
using System.Security.Claims;
using MascovetDomain.Usuarios;
using MascovetWA.Servicios.REST;
using MascovetWA.Components.Layout;
using MascovetWA.Components;

var builder = WebApplication.CreateBuilder(args);

 
builder.Services.AddScoped<MascovetWA.Servicios.LoadingService>();

 
builder.Services.AddScoped(sp =>
{
    var cargando = sp.GetRequiredService<MascovetWA.Servicios.LoadingService>();
    HttpClient client = new HttpClient(new MascovetWA.Servicios.LoadingHttpMessageHandler(cargando));
    client.BaseAddress = new Uri("http://localhost:8080/mascovet_services-1.0-SNAPSHOT/webresources/");
    return client;
});

 
builder.Services.AddRazorComponents()
    .AddInteractiveServerComponents();
builder.Services.AddScoped<MascovetWA.Components.Layout.UserContext>();
builder.Services.AddScoped<MascovetWA.Components.DesignSystem.Feedback.ToastService>();
 
builder.Services.AddHttpContextAccessor();
builder.Services.AddAuthentication(CookieAuthenticationDefaults.AuthenticationScheme)
    .AddCookie(opciones =>
    {
        opciones.LoginPath = "/";
        opciones.AccessDeniedPath = "/acceso-denegado";
        opciones.ExpireTimeSpan = TimeSpan.FromHours(1);
    });
builder.Services.AddAuthorization();
builder.Services.AddCascadingAuthenticationState();
builder.Services.AddScoped<CuentaUsuarioRestService>();
builder.Services.AddScoped<ReporteRestService>();
builder.Services.AddScoped<MascovetWA.Servicios.REST.AdministradorRestService>();
builder.Services.AddScoped<MascovetWA.Servicios.REST.RecepcionistaRestService>();
builder.Services.AddScoped<MascovetWA.Servicios.REST.VeterinarioRestService>();
builder.Services.AddScoped<MascovetWA.Servicios.REST.ClienteRestService>();
builder.Services.AddScoped<MascovetWA.Servicios.REST.MascotaRestService>();
builder.Services.AddScoped<MascovetWA.Servicios.REST.CitaRestService>();
builder.Services.AddScoped<MascovetWA.Servicios.REST.HorarioAtencionRestService>();
builder.Services.AddScoped<MascovetWA.Servicios.REST.ConsultaRestService>();
builder.Services.AddScoped<MascovetWA.Servicios.REST.ControlRestService>();
builder.Services.AddScoped<MascovetWA.Servicios.REST.CirugiaRestService>();
builder.Services.AddScoped<MascovetWA.Servicios.REST.VacunaRestService>();
builder.Services.AddScoped<MascovetWA.Servicios.REST.VacunacionRestService>();
builder.Services.AddScoped<MascovetWA.Servicios.REST.MedicamentoRestService>();
builder.Services.AddScoped<MascovetWA.Servicios.REST.RecetaRestService>();
builder.Services.AddScoped<MascovetWA.Servicios.REST.DetalleRecetaRestService>();

var app = builder.Build();

 
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Error", createScopeForErrors: true);
}
app.UseStatusCodePagesWithReExecute("/not-found", createScopeForStatusCodePages: true);
app.UseAuthentication();
app.UseAuthorization();
app.UseAntiforgery();

app.MapStaticAssets();
 
app.MapPost("/auth/login", async (HttpContext context,
    CuentaUsuarioRestService cuentas, IAntiforgery antiforgery) =>
{
    try { await antiforgery.ValidateRequestAsync(context); }
    catch (AntiforgeryValidationException) { return Results.BadRequest("Solicitud no válida."); }

    var form = await context.Request.ReadFormAsync();
    string usuario = form["usuario"].ToString().Trim();
    string password = form["password"].ToString();
    if (string.IsNullOrWhiteSpace(usuario) || string.IsNullOrWhiteSpace(password))
        return Results.Redirect("/?error=1");

    CuentaUsuario? cuenta;
    try
    {
        cuenta = await cuentas.AutenticarAsync(new CuentaUsuario
        {
            Username = usuario,
            Password = password
        });
    }
    catch (HttpRequestException)
    {
        return Results.Redirect("/?error=2");
    }
    catch (System.Text.Json.JsonException)
    {
        return Results.Redirect("/?error=2");
    }

    if (cuenta is null || !cuenta.Activo || cuenta.IdUsuario <= 0 ||
        !Enum.TryParse<UserRole>(cuenta.Rol, true, out var rol))
        return Results.Redirect("/?error=1");

    var claims = new List<Claim>
    {
        new(ClaimTypes.NameIdentifier, cuenta.IdUsuario.ToString()),
        new(ClaimTypes.Name, cuenta.Nombre ?? cuenta.Username ?? "Usuario"),
        new(ClaimTypes.Role, rol.ToString()),
        new("usuarioVinculado", cuenta.UsuarioVinculado ?? cuenta.Username ?? "")
    };
    var identidad = new ClaimsIdentity(claims, CookieAuthenticationDefaults.AuthenticationScheme);
    await context.SignInAsync(CookieAuthenticationDefaults.AuthenticationScheme,
        new ClaimsPrincipal(identidad));
    return Results.Redirect("/" + NavCatalog.HomeFor(rol));
});

app.MapPost("/auth/logout", async (HttpContext context, IAntiforgery antiforgery) =>
{
    try { await antiforgery.ValidateRequestAsync(context); }
    catch (AntiforgeryValidationException) { return Results.BadRequest("Solicitud no válida."); }
    await context.SignOutAsync(CookieAuthenticationDefaults.AuthenticationScheme);
    return Results.Redirect("/");
});

 
 
 
app.MapPost("/auth/cambiar-contrasena", async (HttpContext context,
    CuentaUsuarioRestService cuentas, IAntiforgery antiforgery) =>
{
    try { await antiforgery.ValidateRequestAsync(context); }
    catch (AntiforgeryValidationException) { return Results.BadRequest("Solicitud no válida."); }

    if (context.User.Identity?.IsAuthenticated != true)
        return Results.Unauthorized();

    string? rol = context.User.FindFirst(ClaimTypes.Role)?.Value;
    string? ruta = rol switch
    {
        "Cliente" => "/cliente/cambiar-contrasena",
        "Veterinario" => "/veterinario/cambiar-contrasena",
        "Recepcionista" => "/recepcion/cambiar-contrasena",
        _ => null
    };
    if (ruta is null) return Results.Forbid();
    if (!int.TryParse(context.User.FindFirst(ClaimTypes.NameIdentifier)?.Value, out int idUsuario)
        || idUsuario <= 0) return Results.Unauthorized();

    var form = await context.Request.ReadFormAsync();
    string anterior = form["contrasenaActual"].ToString();
    string nueva = form["contrasenaNueva"].ToString();
    string confirmacion = form["confirmarContrasena"].ToString();

    if (string.IsNullOrEmpty(anterior) || nueva.Length < 8 || nueva.Length > 255 ||
        nueva != confirmacion || anterior == nueva)
        return Results.Redirect(ruta + "?estado=3");

    try
    {
        int resultado = await cuentas.CambiarContrasenaAsync(new CuentaUsuario
        {
            IdUsuario = idUsuario,  
            Password = anterior,
            NuevaPassword = nueva
        });
        return Results.Redirect(ruta + (resultado > 0 ? "?estado=ok" : "?estado=1"));
    }
    catch (HttpRequestException)
    {
        return Results.Redirect(ruta + "?estado=2");
    }
    catch (System.Text.Json.JsonException)
    {
        return Results.Redirect(ruta + "?estado=2");
    }
});

app.MapRazorComponents<App>()
    .AddInteractiveServerRenderMode();

app.Run();
