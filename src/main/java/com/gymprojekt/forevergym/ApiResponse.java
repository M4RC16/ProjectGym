package com.gymprojekt.forevergym;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse {
    private String message;
    private boolean success;

    public static ApiResponse success(String message) {
        return new ApiResponse(message, true);
    }
}
