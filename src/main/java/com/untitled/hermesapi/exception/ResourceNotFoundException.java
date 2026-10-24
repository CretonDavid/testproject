package com.untitled.hermesapi.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResourceNotFoundException extends ApplicationException {

    private final String errorMessage;
}
