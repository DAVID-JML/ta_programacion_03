using System;

namespace MascovetWA.Servicios
{


    public class LoadingService
    {
        private readonly object sincronizacion = new();
        private int pendientes;

        public event Action? Changed;

        public bool IsLoading
        {
            get
            {
                lock (sincronizacion)
                    return pendientes > 0;
            }
        }

        public void Iniciar()
        {
            lock (sincronizacion)
                pendientes++;
            Changed?.Invoke();
        }

        public void Terminar()
        {
            lock (sincronizacion)
            {
                if (pendientes > 0)
                    pendientes--;
            }
            Changed?.Invoke();
        }
    }
}
