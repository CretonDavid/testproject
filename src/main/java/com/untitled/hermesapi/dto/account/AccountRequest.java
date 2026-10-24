package com.untitled.hermesapi.dto.account;

public record AccountRequest(
        Long accountTypeId,
        String description,
        Long currencyId,
        Double balance
) {
}
