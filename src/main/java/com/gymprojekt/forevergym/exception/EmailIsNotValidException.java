package com.gymprojekt.forevergym.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class EmailIsNotValidException extends RuntimeException {
    public EmailIsNotValidException(String message) {
        super(message);
    }
}
