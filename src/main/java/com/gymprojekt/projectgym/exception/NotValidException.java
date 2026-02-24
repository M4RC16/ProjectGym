package com.gymprojekt.projectgym.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class NotValidException extends RuntimeException {
    public NotValidException(String message) {
        super(message);
    }
}
