using System.Net.Http;
using System.Threading;
using System.Threading.Tasks;

namespace MascovetWA.Servicios
{
     
     
    public class LoadingHttpMessageHandler : DelegatingHandler
    {
        private readonly LoadingService loading;

        public LoadingHttpMessageHandler(LoadingService loading)
            : base(new HttpClientHandler())
        {
            this.loading = loading;
        }

        protected override async Task<HttpResponseMessage> SendAsync(
            HttpRequestMessage request, CancellationToken cancellationToken)
        {
            loading.Iniciar();
            try
            {
                return await base.SendAsync(request, cancellationToken);
            }
            finally
            {
                loading.Terminar();
            }
        }
    }
}
