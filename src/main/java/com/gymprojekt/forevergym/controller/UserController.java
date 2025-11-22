package com.gymprojekt.forevergym.controller;

import com.gymprojekt.forevergym.model.User;
import com.gymprojekt.forevergym.security.JwtService;
import com.gymprojekt.forevergym.service.AuthService;
import com.gymprojekt.forevergym.service.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService service;
    private final AuthService authService;

    public UserController(UserService service, AuthService authService) {
        this.service = service;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registerUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("verify/{token}")
    public ResponseEntity<User> verifyToken(@PathVariable String token) {
        return null;
    }


    @Setter
    @Getter
    public static class LoginRequest {
        private String email;
        private String password;

        public LoginRequest() {}

        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }

    }

    @Setter
    @Getter
    public static class LoginResponse {
        private String token;
        private String email;
        private String message;

        public LoginResponse(String token, String email, String message) {
            this.token = token;
            this.email = email;
            this.message = message;
        }

    }

}
