using System;
using System.Globalization;
using System.Text.Json;
using System.Text.Json.Serialization;

namespace MascovetWA.Servicios.REST
{
     
     
    public class FechaRestJsonConverter : JsonConverter<DateTime?>
    {
        public override DateTime? Read(ref Utf8JsonReader reader, Type typeToConvert, JsonSerializerOptions options)
        {
            if (reader.TokenType == JsonTokenType.Null) return null;

            if (reader.TokenType == JsonTokenType.Number)
            {
                long valor = reader.GetInt64();
                return DesdeUnix(valor);
            }

            if (reader.TokenType == JsonTokenType.String)
            {
                string? texto = reader.GetString();
                if (string.IsNullOrWhiteSpace(texto)) return null;
                if (long.TryParse(texto, NumberStyles.Integer, CultureInfo.InvariantCulture, out long numero))
                    return DesdeUnix(numero);

                 
                int corchete = texto.IndexOf('[');
                if (corchete > 0) texto = texto.Substring(0, corchete);
                if (DateTimeOffset.TryParse(texto, CultureInfo.InvariantCulture,
                    DateTimeStyles.AllowWhiteSpaces, out DateTimeOffset fechaConZona))
                    return fechaConZona.DateTime;
                if (DateTime.TryParse(texto, CultureInfo.InvariantCulture,
                    DateTimeStyles.AllowWhiteSpaces, out DateTime fecha))
                    return fecha;
                throw new JsonException($"Formato de fecha REST no reconocido: {texto}");
            }

            throw new JsonException($"Fecha REST no reconocida (tipo JSON: {reader.TokenType}).");
        }

        private static DateTime DesdeUnix(long valor)
        {
             
            return (valor > 100_000_000_000L || valor < -100_000_000_000L)
                ? DateTimeOffset.FromUnixTimeMilliseconds(valor).UtcDateTime
                : DateTimeOffset.FromUnixTimeSeconds(valor).UtcDateTime;
        }

        public override void Write(Utf8JsonWriter writer, DateTime? value, JsonSerializerOptions options)
        {
            if (!value.HasValue) writer.WriteNullValue();
            else writer.WriteStringValue(value.Value);
        }
    }
}
