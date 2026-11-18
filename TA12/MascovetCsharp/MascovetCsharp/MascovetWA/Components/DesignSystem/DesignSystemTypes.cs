namespace MascovetWA.Components.DesignSystem;

 
public enum ButtonVariant { Primary, Secondary, Tertiary, Danger }

 
public enum ButtonSize { Medium, Small }

 
public enum SemanticVariant { Neutral, Info, Success, Warning, Error }

public static class DesignSystemExtensions
{
     
    public static string ToCss(this Enum value) => value.ToString().ToLowerInvariant();

     
    public static string? IconName(this SemanticVariant variant) => variant switch
    {
        SemanticVariant.Success => "check-circle",
        SemanticVariant.Warning => "alert-triangle",
        SemanticVariant.Error => "alert-circle",
        SemanticVariant.Info => "info",
        _ => null
    };

     
    public static string Label(this SemanticVariant variant) => variant switch
    {
        SemanticVariant.Success => "Éxito",
        SemanticVariant.Warning => "Advertencia",
        SemanticVariant.Error => "Error",
        SemanticVariant.Info => "Información",
        _ => "Estado"
    };
}
