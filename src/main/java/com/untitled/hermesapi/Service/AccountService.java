package com.untitled.hermesapi.Service;

import com.untitled.hermesapi.dto.account.AccountRequest;
import com.untitled.hermesapi.dto.account.AccountResponse;
import com.untitled.hermesapi.model.Account;
import com.untitled.hermesapi.model.AccountType;
import com.untitled.hermesapi.model.Currency;
import com.untitled.hermesapi.repository.AccountRepository;
import com.untitled.hermesapi.repository.AccountTypeRepository;
import com.untitled.hermesapi.repository.CurrencyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final CurrencyRepository currencyRepository;

    public AccountResponse create(AccountRequest request) {
        AccountType accountType = accountTypeRepository.findById(request.accountTypeId())
                .orElseThrow(() -> new EntityNotFoundException("AccountType not found"));

        Currency currency = currencyRepository.findById(request.currencyId())
                .orElseThrow(() -> new EntityNotFoundException("Currency not found"));

        Account account = new Account();
        account.setAccountType(accountType);
        account.setCurrency(currency);
        account.setDescription(request.description());
        account.setBalance(request.balance());

        accountRepository.save(account);
        log.info("{} created successfully", account.getAccountType().getName());

        return mapToResponse(account);
    }

    public AccountResponse update(Long id, AccountRequest request) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        if (request.accountTypeId() != null) {
            AccountType accountType = accountTypeRepository.findById(request.accountTypeId())
                    .orElseThrow(() -> new EntityNotFoundException("AccountType not found"));
            account.setAccountType(accountType);
        }

        if (request.currencyId() != null) {
            Currency currency = currencyRepository.findById(request.currencyId())
                    .orElseThrow(() -> new EntityNotFoundException("Currency not found"));
            account.setCurrency(currency);
        }

        account.setDescription(request.description());
        account.setBalance(request.balance());

        accountRepository.save(account);
        log.info("{} updated successfully", account.getAccountType().getName());

        return mapToResponse(account);
    }

    public void delete(Long id){
        if (!accountRepository.existsById(id)) {
            throw new EntityNotFoundException("Account not found");
        }
        accountRepository.deleteById(id);
        log.info("Account deleted successfully");
    }

    public AccountResponse getById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        return mapToResponse(account);
    }

    public List<AccountResponse> getAll() {
        return accountRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private AccountResponse mapToResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getDescription(),
                account.getBalance(),
                account.getAccountType().getName(),
                account.getCurrency().getCode());
    }
}
