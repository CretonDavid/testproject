package com.untitled.hermesapi.Service;

import com.untitled.hermesapi.model.User;
import com.untitled.hermesapi.repository.UserRepository;
import com.untitled.hermesapi.utils.AccountNumberGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final AccountNumberGenerator accountNumberGenerator;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private static final Pattern ACCOUNT_NUMBER_PATTERN = Pattern.compile("\\d{30}");

    public String createUser() {
        String accountNumber = generateAccountNumber();
        String key = extractKey(accountNumber);
        LocalDateTime createdAt = LocalDateTime.now();

        User user = User.builder()
                .accountNumber(passwordEncoder.encode(accountNumber))
                .key(key)
                .createdAt(createdAt)
                .isEnabled(true)
                .build();

        try {
            userRepository.save(user);
        } catch (DataAccessException exception) {
            log.error("Failed to create user", exception);
        }

        return accountNumber;
    }

    public String generateAccountNumber() {
        String accountNumber;

        do {
            accountNumber = accountNumberGenerator.generate();
        } while (keyAlreadyExists(accountNumber));

        return accountNumber;
    }

    private boolean keyAlreadyExists(String accountNumber) {
        String key = extractKey(accountNumber);
        return userRepository.existsByKey(key);
    }

    public User getUser(String key) {
        return userRepository.getUserByKey(key);
    }

    // La clé représente les 6 premier charactères de l'account number
    public String extractKey(String accountNumber) {
        if (accountNumber == null || !ACCOUNT_NUMBER_PATTERN.matcher(accountNumber).matches()) {
            throw new IllegalArgumentException(
                    "Account number must contain exactly 30 digits"
            );
        }

        return accountNumber.substring(0, 6);
    }
}
