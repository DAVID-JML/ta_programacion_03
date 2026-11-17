using MascovetWA.Components;

var builder = WebApplication.CreateBuilder(args);

// Estado de carga global para las consultas REST Java.
builder.Services.AddScoped<MascovetWA.Servicios.LoadingService>();

// Cliente HTTP como en el ejemplo del profesor, con indicador de carga compartido.
builder.Services.AddScoped(sp =>
{
    var cargando = sp.GetRequiredService<MascovetWA.Servicios.LoadingService>();
    HttpClient client = new HttpClient(new MascovetWA.Servicios.LoadingHttpMessageHandler(cargando));
    client.BaseAddress = new Uri("http://localhost:8080/mascovet_services/webresources/");
    return client;
});

// Add services to the container.
builder.Services.AddRazorComponents()
    .AddInteractiveServerComponents();
builder.Services.AddScoped<MascovetWA.Components.Layout.UserContext>();
builder.Services.AddScoped<MascovetWA.Components.DesignSystem.Feedback.ToastService>();
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
builder.Services.AddScoped<MascovetWA.Servicios.REST.ReservaCitaRestService>();
builder.Services.AddScoped<MascovetWA.Servicios.REST.AuthRestService>();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Error", createScopeForErrors: true);
}
app.UseStatusCodePagesWithReExecute("/not-found", createScopeForStatusCodePages: true);
app.UseAntiforgery();

app.MapStaticAssets();
app.MapRazorComponents<App>()
    .AddInteractiveServerRenderMode();

app.Run();
