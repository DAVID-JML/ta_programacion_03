using System;
using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;

namespace MascovetWA.Services.REST
{
    public class RestBaseService
    {
        protected readonly HttpClient HttpClient;

        public RestBaseService(HttpClient httpClient)
        {
            HttpClient = httpClient;
        }

        protected async Task<T?> GetAsync<T>(string endpoint)
        {
            try
            {
                var response = await HttpClient.GetAsync(endpoint);
                if (response.IsSuccessStatusCode)
                {
                    return await response.Content.ReadFromJsonAsync<T>();
                }
                if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                {
                    return default;
                }
                throw new HttpRequestException($"Error HTTP {response.StatusCode}: {response.ReasonPhrase}");
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException($"Error de conexión REST en GET ({endpoint}): {ex.Message}", ex);
            }
        }

        protected async Task<bool> PostAsync<T>(string endpoint, T data)
        {
            try
            {
                var response = await HttpClient.PostAsJsonAsync(endpoint, data);
                return response.IsSuccessStatusCode;
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException($"Error de conexión REST en POST ({endpoint}): {ex.Message}", ex);
            }
        }

        protected async Task<bool> PutAsync<T>(string endpoint, T data)
        {
            try
            {
                var response = await HttpClient.PutAsJsonAsync(endpoint, data);
                return response.IsSuccessStatusCode;
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException($"Error de conexión REST en PUT ({endpoint}): {ex.Message}", ex);
            }
        }

        protected async Task<bool> DeleteAsync(string endpoint)
        {
            try
            {
                var response = await HttpClient.DeleteAsync(endpoint);
                return response.IsSuccessStatusCode;
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException($"Error de conexión REST en DELETE ({endpoint}): {ex.Message}", ex);
            }
        }
    }
}