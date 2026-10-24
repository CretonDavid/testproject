package com.untitled.hermesapi.dto.currency;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CurrencyRequest(
        @NotBlank(message = "Currency code is required")
        @Size(min = 3, max = 3, message = "Currency code must be 3 characters (e.g., EUR)")
        String code,
        @NotBlank(message = "Currency symbol is required")
        String symbol,
        @NotBlank(message = "Currency name is required")
        String name
) {
}
