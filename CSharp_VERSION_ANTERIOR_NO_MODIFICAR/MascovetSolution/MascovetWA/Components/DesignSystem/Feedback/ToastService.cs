namespace MascovetWA.Components.DesignSystem.Feedback;

/// <summary>Notificación gestionada por ToastService.</summary>
public sealed record ToastMessage(Guid Id, SemanticVariant Variant, string Message, string? Title, int DurationMs);
public sealed class ToastService
{
    private readonly List<ToastMessage> _items = new();

    /// <summary>Se dispara cuando se agrega o se retira una notificación.</summary>
    public event Action? Changed;

    public IReadOnlyList<ToastMessage> Items => _items.ToArray();

    /// <param name="durationMs">Milisegundos visibles. Usa 0 para que solo se cierre manualmente.</param>
    public void Show(SemanticVariant variant, string message, string? title = null, int durationMs = 5000)
    {
        _items.Add(new ToastMessage(Guid.NewGuid(), variant, message, title, durationMs));
        Changed?.Invoke();
    }

    public void Success(string message, string? title = null, int durationMs = 5000) => Show(SemanticVariant.Success, message, title, durationMs);
    public void Info(string message, string? title = null, int durationMs = 5000) => Show(SemanticVariant.Info, message, title, durationMs);
    public void Warning(string message, string? title = null, int durationMs = 5000) => Show(SemanticVariant.Warning, message, title, durationMs);
    public void Error(string message, string? title = null, int durationMs = 5000) => Show(SemanticVariant.Error, message, title, durationMs);

    public void Dismiss(Guid id)
    {
        if (_items.RemoveAll(t => t.Id == id) > 0) Changed?.Invoke();
    }
}
