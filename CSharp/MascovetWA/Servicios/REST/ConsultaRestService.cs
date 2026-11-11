using System.Net.Http.Json;
using System.Text.Json;
using System.Text.Json.Serialization;
using MascovetDomain.AtencionesMedicas;

namespace MascovetWA.Servicios.REST
{
    public class ConsultaRestService
    {
        private readonly HttpClient client;
        private static readonly JsonSerializerOptions opciones = new(JsonSerializerDefaults.Web)
        {
            Converters = { new JsonStringEnumConverter(), new FechaRestJsonConverter() },
            PreferredObjectCreationHandling = JsonObjectCreationHandling.Populate
        };

        public ConsultaRestService(HttpClient client)
        {
            this.client = client;
        }

        public async Task<List<Consulta>> ListarTodosAsync()
        {
            return await client.GetFromJsonAsync<List<Consulta>>("ConsultaRS/listarTodosConsultas", opciones) ?? new List<Consulta>();
        }

        public async Task<Consulta?> BuscarPorIdAsync(int id)
        {
            return await client.GetFromJsonAsync<Consulta>($"ConsultaRS/buscarConsultaPorId/{id}", opciones);
        }

        public async Task<int> InsertarAsync(Consulta entidad)
        {
            HttpResponseMessage response = await client.PostAsJsonAsync("ConsultaRS/insertarConsulta", entidad, opciones);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<int> ModificarAsync(Consulta entidad)
        {
            HttpResponseMessage response = await client.PutAsJsonAsync("ConsultaRS/modificarConsulta", entidad, opciones);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<int> EliminarAsync(int id)
        {
            HttpResponseMessage response = await client.DeleteAsync($"ConsultaRS/eliminarConsulta/{id}");
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }
    }
}
