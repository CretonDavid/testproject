package com.untitled.hermesapi.exception;

import com.untitled.hermesapi.dto.ApiExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiExceptionResponse> handleException(Exception exception) {
        log.error(exception.getMessage());
        final ApiExceptionResponse response = new ApiExceptionResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage());

        return ResponseEntity.status(response.status()).body(response);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiExceptionResponse> handleHermesException(ApplicationException exception) {
        log.error(exception.getMessage());
        final ApiExceptionResponse response = new ApiExceptionResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage());

        return ResponseEntity.status(response.status()).body(response);
    }
}
