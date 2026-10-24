package com.untitled.hermesapi.Service;

import com.untitled.hermesapi.dto.LoginRequest;
import com.untitled.hermesapi.dto.LoginResponse;
import com.untitled.hermesapi.dto.RegisterResponse;
import com.untitled.hermesapi.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;

    // Your accountNumber is the only way to access your account.
    // Store it somewhere safe. We cannot recover it for you.
    public RegisterResponse register() {
        return new RegisterResponse(userService.createUser());
    }

    public LoginResponse login(LoginRequest request) {
        String key = userService.extractKey(request.accountNumber());
        User user = userService.getUser(key);
        String jwtToken = jwtService.generateAccessToken(user.getId());
        String refreshToken = jwtService.generateRefreshToken(user.getId());

        return new LoginResponse(jwtToken,refreshToken);
    }

}
