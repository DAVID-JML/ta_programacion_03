using System.Net.Http.Json;
using System.Text.Json;
using System.Text.Json.Serialization;
using MascovetDomain.Recetas;

namespace MascovetWA.Servicios.REST
{
    public class RecetaRestService
    {
        private readonly HttpClient client;
        private static readonly JsonSerializerOptions opciones = new(JsonSerializerDefaults.Web)
        {
            Converters = { new JsonStringEnumConverter(), new FechaRestJsonConverter() },
            PreferredObjectCreationHandling = JsonObjectCreationHandling.Populate
        };

        public RecetaRestService(HttpClient client)
        {
            this.client = client;
        }

        // La respuesta de JSON-B contiene una atención concreta dentro del tipo abstracto
        // AtencionMedica. Leer primero su identificador, sin intentar construir la clase abstracta.
        public async Task<List<Receta>> ListarTodosAsync()
        {
            using HttpResponseMessage respuesta = await client.GetAsync("RecetaRS/listarTodosRecetas");
            respuesta.EnsureSuccessStatusCode();
            using JsonDocument documento = JsonDocument.Parse(await respuesta.Content.ReadAsStringAsync());
            var resultado = new List<Receta>();
            if (documento.RootElement.ValueKind != JsonValueKind.Array) return resultado;
            foreach (JsonElement elemento in documento.RootElement.EnumerateArray())
            {
                if (elemento.ValueKind != JsonValueKind.Object) continue;
                Receta receta = LeerReceta(elemento);
                resultado.Add(receta);
            }
            return resultado;
        }

        public async Task<Receta?> BuscarPorIdAsync(int id)
        {
            using HttpResponseMessage respuesta = await client.GetAsync($"RecetaRS/buscarRecetaPorId/{id}");
            respuesta.EnsureSuccessStatusCode();
            string contenido = await respuesta.Content.ReadAsStringAsync();
            if (string.IsNullOrWhiteSpace(contenido) || contenido.Trim() == "null") return null;
            using JsonDocument documento = JsonDocument.Parse(contenido);
            return LeerReceta(documento.RootElement);
        }

        private static Receta LeerReceta(JsonElement elemento)
        {
            Receta receta = new();
            if (elemento.TryGetProperty("idReceta", out JsonElement id)
                && id.ValueKind == JsonValueKind.Number) receta.IdReceta = id.GetInt32();
            if (elemento.TryGetProperty("indicaciones", out JsonElement indicaciones)
                && indicaciones.ValueKind == JsonValueKind.String) receta.Indicaciones = indicaciones.GetString();
            if (elemento.TryGetProperty("fecha", out JsonElement fecha)
                && fecha.ValueKind != JsonValueKind.Null)
                receta.Fecha = JsonSerializer.Deserialize<DateTime?>(fecha.GetRawText(), opciones);
            if (elemento.TryGetProperty("activo", out JsonElement activo)
                && activo.ValueKind is JsonValueKind.True or JsonValueKind.False)
                receta.Activo = activo.GetBoolean();
            if (elemento.TryGetProperty("atencionMedica", out JsonElement atencion)
                && atencion.ValueKind == JsonValueKind.Object
                && atencion.TryGetProperty("idAtencion", out JsonElement idAtencion)
                && idAtencion.ValueKind == JsonValueKind.Number)
                receta.AtencionMedica = new MascovetDomain.AtencionesMedicas.Consulta
                {
                    IdAtencion = idAtencion.GetInt32()
                };
            return receta;
        }

        public async Task<int> InsertarAsync(Receta entidad)
        {
            HttpResponseMessage response = await client.PostAsJsonAsync("RecetaRS/insertarReceta", entidad, opciones);
            if (!response.IsSuccessStatusCode)
            {
                string cuerpo = await response.Content.ReadAsStringAsync();
                throw new HttpRequestException($"REST {((int)response.StatusCode)} al guardar receta. " +
                    (cuerpo.Contains("Internal Server Error", StringComparison.OrdinalIgnoreCase)
                    ? "Revisa RecetaRS y RecetaBOImpl en GlassFish (server.log)."
                    : cuerpo[..Math.Min(250, cuerpo.Length)]));
            }
            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<int> ModificarAsync(Receta entidad)
        {
            HttpResponseMessage response = await client.PutAsJsonAsync("RecetaRS/modificarReceta", entidad, opciones);
            if (!response.IsSuccessStatusCode)
                throw new HttpRequestException($"REST {((int)response.StatusCode)}: Revisa el error en GlassFish (RecetaRS).");
            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<int> EliminarAsync(int id)
        {
            HttpResponseMessage response = await client.DeleteAsync($"RecetaRS/eliminarReceta/{id}");
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }

        public async Task<int> InsertarConDetallesAsync(Receta receta)
        {
            if (receta.AtencionMedica == null || receta.AtencionMedica.IdAtencion <= 0)
                throw new InvalidOperationException("Selecciona una atención médica válida.");
            if (!receta.Fecha.HasValue)
                throw new InvalidOperationException("La fecha de la receta es obligatoria.");
            if (receta.DetallesReceta.Count == 0)
                throw new InvalidOperationException("Agrega al menos un medicamento.");

            string tipoAtencion = receta.AtencionMedica switch
            {
                MascovetDomain.AtencionesMedicas.Consulta => "CONSULTA",
                MascovetDomain.AtencionesMedicas.Control => "CONTROL",
                MascovetDomain.AtencionesMedicas.Cirugia => "CIRUGIA",
                MascovetDomain.AtencionesMedicas.Vacunacion => "VACUNACION",
                _ => throw new InvalidOperationException("Tipo de atención no válido.")
            };

            int idAtencion = receta.AtencionMedica.IdAtencion;
            string fecha = receta.Fecha.Value.ToString("yyyy-MM-dd");
            string indicaciones = Uri.EscapeDataString(receta.Indicaciones ?? "");
            string url = $"RecetaRS/insertarConDetalles/{idAtencion}/{tipoAtencion}" +
                         $"?fecha={fecha}&indicaciones={indicaciones}";

            HttpResponseMessage response = await client.PostAsJsonAsync(
                url, receta.DetallesReceta, opciones);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<int>();
        }
    }
}
