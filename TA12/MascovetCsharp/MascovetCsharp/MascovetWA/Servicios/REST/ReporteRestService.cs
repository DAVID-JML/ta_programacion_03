namespace MascovetWA.Servicios.REST
{
     
    public class ReporteRestService
    {
        private readonly HttpClient client;

        public ReporteRestService(HttpClient client)
        {
            this.client = client;
        }

        public async Task<byte[]> GenerarCitasPorTipoAsync(DateOnly inicio, DateOnly fin)
        {
            var url = $"ReporteRS/citasPorTipoAtencion?fechaInicio={inicio:yyyy-MM-dd}&fechaFin={fin:yyyy-MM-dd}";
            using var respuesta = await client.GetAsync(url);
            respuesta.EnsureSuccessStatusCode();
            return await respuesta.Content.ReadAsByteArrayAsync();
        }

        public async Task<byte[]> GenerarCitasPorEspecialidadAsync(DateOnly inicio, DateOnly fin)
        {
            var url = $"ReporteRS/citasPorEspecialidad?fechaInicio={inicio:yyyy-MM-dd}&fechaFin={fin:yyyy-MM-dd}";
            using var respuesta = await client.GetAsync(url);
            respuesta.EnsureSuccessStatusCode();
            return await respuesta.Content.ReadAsByteArrayAsync();
        }

        public async Task<byte[]> GenerarHistorialClinicoAsync(int mascotaId, DateOnly? inicio, DateOnly? fin)
        {
            var url = $"ReporteRS/historialClinico?idMascota={mascotaId}";
            if (inicio.HasValue) url += $"&fechaInicio={inicio.Value:yyyy-MM-dd}";
            if (fin.HasValue) url += $"&fechaFin={fin.Value:yyyy-MM-dd}";
            using var respuesta = await client.GetAsync(url);
            respuesta.EnsureSuccessStatusCode();
            return await respuesta.Content.ReadAsByteArrayAsync();
        }
    }
}
