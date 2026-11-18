using System.Diagnostics.CodeAnalysis;
using System.Globalization;
using Microsoft.AspNetCore.Components;
using Microsoft.AspNetCore.Components.Forms;

namespace MascovetWA.Components.DesignSystem.Forms;

 
 
 
 
 
public abstract class AppFieldBase<TValue> : InputBase<TValue>
{
    private readonly string _generatedId = "ms-field-" + Guid.NewGuid().ToString("N")[..8];

     
    [Parameter] public string? Id { get; set; }
    [Parameter] public string? Label { get; set; }
    [Parameter] public string? HelpText { get; set; }
    [Parameter] public string? Placeholder { get; set; }

     
    [Parameter] public string? ErrorMessage { get; set; }
    [Parameter] public bool Disabled { get; set; }

     
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
