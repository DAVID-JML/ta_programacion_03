using System.Net.Http.Json;
using System.Text.Json;
using System.Text.Json.Serialization;
using MascovetDomain.Recetas;

namespace MascovetWA.Servicios.REST
{
    public class MedicamentoRestService
    {
        private readonly HttpClient client;
        private static readonly JsonSerializerOptions opciones = new(JsonSerializerDefaults.Web)
        {
            Converters = { new JsonStringEnumConverter(), new FechaRestJsonConverter() },
            PreferredObjectCreationHandling = JsonObjectCreationHandling.Populate
        };

        public MedicamentoRestService(HttpClient client)
        {
            this.client = client;
        }

        public async Task<List<Medicamento>> ListarTodosAsync()
        {
            return await client.GetFromJsonAsync<List<Medicamento>>("MedicamentoRS/listarTodosMedicamentos", opciones) ?? new List<Medicamento>();
        }

        public async Task<Medicamento?> BuscarPorIdAsync(int id)
        {
            return await client.GetFromJsonAsync<Medicamento>($"MedicamentoRS/buscarMedicamentoPorId/{id}", opciones);
        }

        public async Task<int> InsertarAsync(Medicamento entidad)
        {
            HttpResponseMessage response = await client.PostAsJsonAsync("MedicamentoRS/insertarMedicamento", entidad, opciones);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<int> ModificarAsync(Medicamento entidad)
        {
            HttpResponseMessage response = await client.PutAsJsonAsync("MedicamentoRS/modificarMedicamento", entidad, opciones);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<int> EliminarAsync(int id)
        {
            HttpResponseMessage response = await client.DeleteAsync($"MedicamentoRS/eliminarMedicamento/{id}");
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }
    }
}
