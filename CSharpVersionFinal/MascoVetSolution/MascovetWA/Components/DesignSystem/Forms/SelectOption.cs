namespace MascovetWA.Components.DesignSystem.Forms;

/// <summary>Opción de AppSelect. Value es el valor tipado; Text, lo que ve la persona.</summary>
public sealed record SelectOption<TValue>(TValue Value, string Text, bool Disabled = false);
