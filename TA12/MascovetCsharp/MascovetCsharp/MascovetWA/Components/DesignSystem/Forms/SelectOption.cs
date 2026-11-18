namespace MascovetWA.Components.DesignSystem.Forms;

 
public sealed record SelectOption<TValue>(TValue Value, string Text, bool Disabled = false);
