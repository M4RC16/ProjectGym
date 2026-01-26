package com.gymprojekt.forevergym.controller;

import com.gymprojekt.forevergym.UserProjection;
import com.gymprojekt.forevergym.model.User;
import com.gymprojekt.forevergym.service.AuthService;
import com.gymprojekt.forevergym.service.EmailService;
import com.gymprojekt.forevergym.service.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService service;
    private final AuthService authService;
    private final EmailService emailService;

    public UserController(UserService service, AuthService authService, EmailService emailService) {
        this.service = service;
        this.authService = authService;
        this.emailService = emailService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registerUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sendForgottenPassEmail")
    public ResponseEntity<?> sendForgottenPassEmail(@RequestBody EmailRequest request) {
        String result = emailService.SendPasswordResetEmail(request.getEmail());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest request) {
        return ResponseEntity.ok(service.resetPassword(request));
    }

    @GetMapping("verify/{token}")
    public ResponseEntity<String> verifyToken(@PathVariable String token) {
        String result = service.verifyUser(token);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/requests/getAllUser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserProjection>> getAllUserForAdmin() {
        return ResponseEntity.ok(service.getAllUsers());
    }

    @PostMapping("/change/Role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> changeRole(@RequestBody changeUserRole changeUser) {
        String response = service.changeUserRole(changeUser);
        return ResponseEntity.ok(response);
    }

    @Setter
    @Getter
    public static class LoginRequest {
        private String email;
        private String password;

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

    @Setter
    @Getter
    public static class EmailRequest {
        private String email;
    }

    @Setter
    @Getter
    public static class PasswordResetRequest {
        private String token;
        private String password1;
        private String password2;

        public PasswordResetRequest(String token, String password1, String password2) {
            this.token = token;
            this.password1 = password1;
            this.password2 = password2;
        }
    }

    @Setter
    @Getter
    public static class changeUserRole {
        private int id;
        private Integer roleId;

        public changeUserRole(int id, Integer roleId) {
            this.id = id;
            this.roleId = roleId;
        }
    }

}
