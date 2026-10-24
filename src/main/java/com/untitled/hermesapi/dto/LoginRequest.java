package com.untitled.hermesapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginRequest(

        @NotBlank
        @Pattern(regexp = "\\d{30}",
                message = "Account number must contain exactly 30 digits")
        String accountNumber

){
}
