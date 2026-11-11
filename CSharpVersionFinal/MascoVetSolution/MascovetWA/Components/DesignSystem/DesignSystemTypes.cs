namespace MascovetWA.Components.DesignSystem;

/// <summary>Variantes visuales de AppButton.</summary>
public enum ButtonVariant { Primary, Secondary, Tertiary, Danger }

/// <summary>Tamaños de AppButton.</summary>
public enum ButtonSize { Medium, Small }

/// <summary>Variantes semánticas compartidas por AppAlert, AppToast y AppBadge.</summary>
public enum SemanticVariant { Neutral, Info, Success, Warning, Error }

public static class DesignSystemExtensions
{
    /// <summary>Nombre en minúsculas para construir clases CSS (p. ej. "primary").</summary>
    public static string ToCss(this Enum value) => value.ToString().ToLowerInvariant();

    /// <summary>Icono asociado a la variante (null en Neutral).</summary>
    public static string? IconName(this SemanticVariant variant) => variant switch
    {
        SemanticVariant.Success => "check-circle",
        SemanticVariant.Warning => "alert-triangle",
        SemanticVariant.Error => "alert-circle",
        SemanticVariant.Info => "info",
        _ => null
    };

    /// <summary>Etiqueta de texto que acompaña al color para que el estado no dependa solo de él.</summary>
    public static string Label(this SemanticVariant variant) => variant switch
    {
        SemanticVariant.Success => "Éxito",
        SemanticVariant.Warning => "Advertencia",
        SemanticVariant.Error => "Error",
        SemanticVariant.Info => "Información",
        _ => "Estado"
    };
}