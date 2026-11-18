using System.Net.Http.Json;
using System.Text.Json;
using System.Text.Json.Serialization;
using MascovetDomain.Citas;

namespace MascovetWA.Servicios.REST
{
    public class CitaRestService
    {
        private readonly HttpClient client;
        private static readonly JsonSerializerOptions opciones = new(JsonSerializerDefaults.Web)
        {
            Converters = { new JsonStringEnumConverter(), new FechaRestJsonConverter() },
            PreferredObjectCreationHandling = JsonObjectCreationHandling.Populate
        };

        public CitaRestService(HttpClient client)
        {
            this.client = client;
        }

        public async Task<List<Cita>> ListarTodosAsync()
        {
            return await client.GetFromJsonAsync<List<Cita>>("CitaRS/listarTodosCitas", opciones) ?? new List<Cita>();
        }

        public async Task<Cita?> BuscarPorIdAsync(int id)
        {
            return await client.GetFromJsonAsync<Cita>($"CitaRS/buscarCitaPorId/{id}", opciones);
        }

        public async Task<int> InsertarAsync(Cita entidad)
        {
            HttpResponseMessage response = await client.PostAsJsonAsync("CitaRS/insertarCita", entidad, opciones);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<int> ModificarAsync(Cita entidad)
        {
            HttpResponseMessage response = await client.PutAsJsonAsync("CitaRS/modificarCita", entidad, opciones);
            if (!response.IsSuccessStatusCode)
            {
                throw new HttpRequestException($"REST {((int)response.StatusCode)} al actualizar la cita. " +
                    "Verifica en CitaBOImpl si la cita está RESERVADA, si la fecha es futura y si el horario está disponible. " +
                    "Para un error 500 revisa el server.log de GlassFish.");
            }
            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<int> EliminarAsync(int id)
        {
            HttpResponseMessage response = await client.DeleteAsync($"CitaRS/eliminarCita/{id}");
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }
    }
}
