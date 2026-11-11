using System;

namespace MascovetWA.Servicios
{
    // Indica a las pantallas cuando hay solicitudes REST pendientes.
    // El contador permite varias consultas simultáneas sin ocultar el indicador antes de tiempo.
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
