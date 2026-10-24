package com.untitled.hermesapi.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ApiExceptionResponse(
        LocalDateTime timestamp,
        HttpStatus status,
        String message
) {
}
