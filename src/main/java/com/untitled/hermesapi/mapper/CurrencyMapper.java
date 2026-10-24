package com.untitled.hermesapi.mapper;

import com.untitled.hermesapi.dto.currency.CurrencyRequest;
import com.untitled.hermesapi.dto.currency.CurrencyResponse;
import com.untitled.hermesapi.model.Currency;
import org.springframework.stereotype.Component;

@Component
public class CurrencyMapper {

    public Currency toEntity(CurrencyRequest request) {
        return Currency.builder()
                .code(request.code().toUpperCase())
                .symbol(request.symbol())
                .name(request.name().toUpperCase())
                .build();
    }

    public CurrencyResponse toResponse(Currency entity) {
        return new CurrencyResponse(
                entity.getId(),
                entity.getCode(),
                entity.getSymbol(),
                entity.getName()
        );
    }
}
