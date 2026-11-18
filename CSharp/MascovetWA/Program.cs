using MascovetWA.Components;
using MascovetWA.Servicios.REST;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddScoped<MascovetWA.Servicios.LoadingService>();

builder.Services.AddScoped(sp =>
{
    var cargando = sp.GetRequiredService<MascovetWA.Servicios.LoadingService>();
    HttpClient client = new HttpClient(new MascovetWA.Servicios.LoadingHttpMessageHandler(cargando));
    client.BaseAddress = new Uri("http://localhost:8080/mascovet_services/webresources/");
    return client;
});

builder.Services.AddRazorComponents()
    .AddInteractiveServerComponents();
builder.Services.AddScoped<MascovetWA.Components.Layout.UserContext>();
builder.Services.AddScoped<MascovetWA.Components.DesignSystem.Feedback.ToastService>();

builder.Services.AddScoped<AuthRestService>();
builder.Services.AddScoped<ReporteRestService>();
builder.Services.AddScoped<AdministradorRestService>();
builder.Services.AddScoped<RecepcionistaRestService>();
builder.Services.AddScoped<VeterinarioRestService>();
builder.Services.AddScoped<ClienteRestService>();
builder.Services.AddScoped<MascotaRestService>();
builder.Services.AddScoped<CitaRestService>();
builder.Services.AddScoped<HorarioAtencionRestService>();
builder.Services.AddScoped<ConsultaRestService>();
builder.Services.AddScoped<ControlRestService>();
builder.Services.AddScoped<CirugiaRestService>();
builder.Services.AddScoped<VacunaRestService>();
builder.Services.AddScoped<VacunacionRestService>();
builder.Services.AddScoped<MedicamentoRestService>();
builder.Services.AddScoped<RecetaRestService>();
builder.Services.AddScoped<DetalleRecetaRestService>();

var app = builder.Build();

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