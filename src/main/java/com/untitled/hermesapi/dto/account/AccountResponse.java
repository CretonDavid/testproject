package com.untitled.hermesapi.dto.account;

public record AccountResponse(
        Long id,
        String description,
        Double balance,
        String accountType,
        String currency
) {
}
