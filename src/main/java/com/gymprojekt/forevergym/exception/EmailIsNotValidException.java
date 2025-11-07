package com.gymprojekt.forevergym.exception;

public class EmailIsNotValidException extends RuntimeException {
    public EmailIsNotValidException(String message) {
        super(message);
    }
}
