using MascovetWA.Components;
using MascovetWA.Components.Layout;
using MascovetWA.Components.DesignSystem.Feedback; // Namespace necesario para ToastService
using MascovetWA.Services.REST;

var builder = WebApplication.CreateBuilder(args);

// 1. Configuración de componentes Razor e Invocación Server
builder.Services.AddRazorComponents()
    .AddInteractiveServerComponents();

// 2. Servicios de interfaz, sesión y notificaciones UI (Inyección de Dependencias)
builder.Services.AddScoped<UserContext>();
builder.Services.AddScoped<AuthService>();
builder.Services.AddScoped<ToastService>();

// 3. Configuración de HttpClient base hacia el backend Java JAX-RS
builder.Services.AddScoped(sp => new HttpClient
{
    BaseAddress = new Uri("http://localhost:8080/mascovet_services/webresources/")
});

// 4. Registrar infraestructura base para servicios REST
builder.Services.AddScoped<RestBaseService>();

var app = builder.Build();

if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Error", createScopeForErrors: true);
    app.UseHsts();
}

app.UseHttpsRedirection();
app.UseStaticFiles();
app.UseAntiforgery();

app.MapRazorComponents<App>()
    .AddInteractiveServerRenderMode();

app.Run();