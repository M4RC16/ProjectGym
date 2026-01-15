package com.gymprojekt.forevergym.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PasswordProblemException extends RuntimeException {
    public PasswordProblemException(String message) {
        super(message);
    }
}
