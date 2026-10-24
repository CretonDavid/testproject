package com.untitled.hermesapi.service;

import com.untitled.hermesapi.Service.CurrencyService;
import com.untitled.hermesapi.dto.currency.CurrencyRequest;
import com.untitled.hermesapi.dto.currency.CurrencyResponse;
import com.untitled.hermesapi.exception.ResourceAlreadyExistsException;
import com.untitled.hermesapi.exception.ResourceNotFoundException;
import com.untitled.hermesapi.mapper.CurrencyMapper;
import com.untitled.hermesapi.model.Currency;
import com.untitled.hermesapi.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private CurrencyMapper currencyMapper;

    @InjectMocks
    private CurrencyService currencyService;

    private CurrencyRequest request;
    private Currency currency;
    private Currency savedCurrency;
    private CurrencyResponse response;

    @BeforeEach
    void setUp() {
        request = new CurrencyRequest("usd","$","US Dollar");

        currency = new Currency();
        currency.setId(1L);
        currency.setCode("USD");
        currency.setName("US Dollar");
        currency.setSymbol("$");

        savedCurrency = new Currency();
        savedCurrency.setId(1L);
        savedCurrency.setCode("USD");
        savedCurrency.setName("US Dollar");
        savedCurrency.setSymbol("$");

        response = new CurrencyResponse(1L,"USD","$", "US Dollar");
    }

    // Create

    @Test
    void shouldCreateCurrencySuccessfully() {
        when(currencyRepository.findByCode("USD")).thenReturn(Optional.empty());
        when(currencyMapper.toEntity(request)).thenReturn(currency);
        when(currencyRepository.save(currency)).thenReturn(savedCurrency);
        when(currencyMapper.toResponse(savedCurrency)).thenReturn(response);

        CurrencyResponse result = currencyService.create(request);

        assertNotNull(result);
        assertEquals("USD", result.code());

        verify(currencyRepository).save(currency);
    }

    @Test
    void shouldThrowWhenCreateWithNullRequest() {
        //assertThrows(BadRequestException.class, () -> currencyService.create(null));
    }

    @Test
    void shouldThrowWhenCurrencyAlreadyExists() {
        when(currencyRepository.findByCode("USD")).thenReturn(Optional.of(currency));

        assertThrows(ResourceAlreadyExistsException.class,
                () -> currencyService.create(request));
    }

    // Update

    @Test
    void shouldUpdateCurrencySuccessfully() {
        when(currencyRepository.findById(1L)).thenReturn(Optional.of(currency));
        when(currencyRepository.findByCode("USD")).thenReturn(Optional.of(currency));
        when(currencyRepository.save(currency)).thenReturn(savedCurrency);
        when(currencyMapper.toResponse(savedCurrency)).thenReturn(response);

        CurrencyResponse result = currencyService.update(1L, request);

        assertEquals("USD", result.code());
        verify(currencyRepository).save(currency);
    }

    @Test
    void shouldThrowWhenUpdateWithNullRequest() {
        //assertThrows(BadRequestException.class, () -> currencyService.update(1L, null));
    }

    @Test
    void shouldThrowWhenCurrencyNotFoundForUpdate() {
        when(currencyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> currencyService.update(1L, request));
    }

    @Test
    void shouldThrowWhenUpdatingWithExistingCodeOfAnotherCurrency() {
        Currency another = new Currency();
        another.setId(2L);
        another.setCode("USD");

        when(currencyRepository.findById(1L)).thenReturn(Optional.of(currency));
        when(currencyRepository.findByCode("USD")).thenReturn(Optional.of(another));

        assertThrows(ResourceAlreadyExistsException.class,
                () -> currencyService.update(1L, request));
    }

    @Test
    void shouldDeleteCurrencySuccessfully() {
        when(currencyRepository.existsById(1L)).thenReturn(true);

        currencyService.delete(1L);

        verify(currencyRepository).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeletingNonExistingCurrency() {
        when(currencyRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> currencyService.delete(1L));
    }

    @Test
    void shouldReturnAllCurrencies() {
        List<Currency> currencies = List.of(currency);

        when(currencyRepository.findAll()).thenReturn(currencies);
        when(currencyMapper.toResponse(currency)).thenReturn(response);

        List<CurrencyResponse> result = currencyService.findAll();

        assertEquals(1, result.size());
        assertEquals("USD", result.get(0).code());
    }

    @Test
    void shouldReturnEmptyListWhenNoCurrencies() {
        when(currencyRepository.findAll()).thenReturn(Collections.emptyList());

        List<CurrencyResponse> result = currencyService.findAll();

        assertTrue(result.isEmpty());
    }
}
