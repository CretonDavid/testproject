package com.untitled.hermesapi.service;

import com.untitled.hermesapi.Service.AccountService;
import com.untitled.hermesapi.dto.account.AccountRequest;
import com.untitled.hermesapi.dto.account.AccountResponse;
import com.untitled.hermesapi.model.Account;
import com.untitled.hermesapi.model.AccountType;
import com.untitled.hermesapi.model.Currency;
import com.untitled.hermesapi.repository.AccountRepository;
import com.untitled.hermesapi.repository.AccountTypeRepository;
import com.untitled.hermesapi.repository.CurrencyRepository;
import jakarta.persistence.EntityNotFoundException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountTypeRepository accountTypeRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private AccountService accountService;

    private AccountRequest request;
    private Account account;
    private AccountType accountType;
    private Currency currency;

    @BeforeEach
    void setUp() {
        accountType = new AccountType();
        accountType.setId(1L);
        accountType.setName("SAVINGS");

        currency = new Currency();
        currency.setId(1L);
        currency.setCode("USD");

        request = new AccountRequest(1L,"Test account",1L, Double.valueOf(1000));

        account = new Account();
        account.setId(1L);
        account.setAccountType(accountType);
        account.setCurrency(currency);
        account.setDescription("Test account");
        account.setBalance(Double.valueOf(1000));
    }

    @Test
    void shouldCreateAccountSuccessfully() {
        when(accountTypeRepository.findById(1L)).thenReturn(Optional.of(accountType));
        when(currencyRepository.findById(1L)).thenReturn(Optional.of(currency));
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

        AccountResponse response = accountService.create(request);

        assertNotNull(response);
        assertEquals("SAVINGS", response.accountType());
        assertEquals("USD", response.currency());

        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void shouldThrowWhenAccountTypeNotFoundOnCreate() {
        when(accountTypeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> accountService.create(request));
    }

    @Test
    void shouldThrowWhenCurrencyNotFoundOnCreate() {
        when(accountTypeRepository.findById(1L)).thenReturn(Optional.of(accountType));
        when(currencyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> accountService.create(request));
    }

    @Test
    void shouldUpdateAccountSuccessfully() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountTypeRepository.findById(1L)).thenReturn(Optional.of(accountType));
        when(currencyRepository.findById(1L)).thenReturn(Optional.of(currency));
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

        AccountResponse response = accountService.update(1L, request);

        assertEquals("SAVINGS", response.accountType());
        assertEquals("USD", response.currency());

        verify(accountRepository).save(account);
    }

    @Test
    void shouldThrowWhenAccountNotFoundOnUpdate() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> accountService.update(1L, request));
    }

    @Test
    void shouldUpdateWithoutChangingTypeAndCurrency() {
        AccountRequest partialRequest = new AccountRequest(null,"Updated",null,Double.valueOf(2000));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

        AccountResponse response = accountService.update(1L, partialRequest);

        assertEquals("Updated", response.description());
        assertEquals(Double.valueOf(2000), response.balance());

        verify(accountTypeRepository, never()).findById(any());
        verify(currencyRepository, never()).findById(any());
    }

    @Test
    void shouldThrowWhenAccountTypeNotFoundOnUpdate() {
        //request.accountTypeId(2L);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountTypeRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> accountService.update(1L, request));
    }

    @Test
    void shouldThrowWhenCurrencyNotFoundOnUpdate() {
        //request.currencyId(2L);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(currencyRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> accountService.update(1L, request));
    }

    @Test
    void shouldDeleteAccountSuccessfully() {
        when(accountRepository.existsById(1L)).thenReturn(true);

        accountService.delete(1L);

        verify(accountRepository).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeletingNonExistingAccount() {
        when(accountRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> accountService.delete(1L));
    }

    @Test
    void shouldReturnAccountById() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        AccountResponse response = accountService.getById(1L);

        assertEquals("SAVINGS", response.accountType());
    }

    @Test
    void shouldThrowWhenAccountNotFoundById() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> accountService.getById(1L));
    }

    @Test
    void shouldReturnAllAccounts() {
        when(accountRepository.findAll()).thenReturn(List.of(account));

        List<AccountResponse> result = accountService.getAll();

        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoAccounts() {
        when(accountRepository.findAll()).thenReturn(Collections.emptyList());

        List<AccountResponse> result = accountService.getAll();

        assertTrue(result.isEmpty());
    }
}
