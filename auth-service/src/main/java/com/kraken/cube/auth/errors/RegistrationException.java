package com.kraken.cube.auth.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RegistrationException extends Exception {
    public RegistrationException(String message) {
        super(message);
    }
}
