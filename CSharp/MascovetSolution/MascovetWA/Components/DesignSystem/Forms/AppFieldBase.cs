using System.Diagnostics.CodeAnalysis;
using System.Globalization;
using Microsoft.AspNetCore.Components;
using Microsoft.AspNetCore.Components.Forms;

namespace MascovetWA.Components.DesignSystem.Forms;

/// <summary>
/// Base común de AppInput, AppSelect, AppTextArea y AppDatePicker.
/// Hereda de InputBase&lt;T&gt;, por lo que funciona con @bind-Value y con EditForm / DataAnnotations.
/// Centraliza etiqueta, ayuda, errores, identificadores y atributos ARIA.
/// </summary>
public abstract class AppFieldBase<TValue> : InputBase<TValue>
{
    private readonly string _generatedId = "ms-field-" + Guid.NewGuid().ToString("N")[..8];

    /// <summary>Id del control. Si no se indica se genera uno único.</summary>
    [Parameter] public string? Id { get; set; }
    [Parameter] public string? Label { get; set; }
    [Parameter] public string? HelpText { get; set; }
    [Parameter] public string? Placeholder { get; set; }

    /// <summary>Error explícito (fuera de EditForm). Dentro de un EditForm se muestran además los mensajes de validación.</summary>
    [Parameter] public string? ErrorMessage { get; set; }
    [Parameter] public bool Disabled { get; set; }

    /// <summary>Marca visual (*) y aria-required. No usa el atributo nativo "required" para que mande la validación de Blazor.</summary>
    [Parameter] public bool Required { get; set; }

    protected string FieldId => Id ?? _generatedId;

    protected IReadOnlyList<string> Errors
    {
        get
        {
            var list = new List<string>();
            if (!string.IsNullOrWhiteSpace(ErrorMessage)) list.Add(ErrorMessage);
            if (EditContext is not null) list.AddRange(EditContext.GetValidationMessages(FieldIdentifier));
            return list;
        }
    }

    protected bool HasError => Errors.Count > 0;

    protected string ControlClass => HasError ? "ms-control ms-control--invalid" : "ms-control";

    protected string? DescribedBy
    {
        get
        {
            var ids = new List<string>();
            if (!string.IsNullOrEmpty(HelpText)) ids.Add($"{FieldId}-help");
            if (HasError) ids.Add($"{FieldId}-error");
            return ids.Count == 0 ? null : string.Join(' ', ids);
        }
    }

    protected override string? FormatValueAsString(TValue? value) => value switch
    {
        null => null,
        IFormattable f => f.ToString(null, CultureInfo.InvariantCulture),
        _ => value.ToString()
    };

    protected override bool TryParseValueFromString(string? value, [MaybeNullWhen(false)] out TValue result,
        [NotNullWhen(false)] out string? validationErrorMessage)
    {
        if (typeof(TValue) == typeof(string))
        {
            result = (TValue)(object)(value ?? string.Empty);
            validationErrorMessage = null;
            return true;
        }

        if (BindConverter.TryConvertTo<TValue>(value, CultureInfo.InvariantCulture, out var parsed))
        {
            result = parsed;
            validationErrorMessage = null;
            return true;
        }

        result = default;
        validationErrorMessage = $"{DisplayName ?? Label ?? "El valor"} no es un valor válido.";
        return false;
    }
}
