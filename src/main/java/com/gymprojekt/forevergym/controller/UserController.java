package com.gymprojekt.forevergym.controller;

import com.gymprojekt.forevergym.ApiResponse;
import com.gymprojekt.forevergym.UserProjection;
import com.gymprojekt.forevergym.model.Role;
import com.gymprojekt.forevergym.model.User;
import com.gymprojekt.forevergym.service.AuthService;
import com.gymprojekt.forevergym.service.EmailService;
import com.gymprojekt.forevergym.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
@RestController
@RequestMapping("/api/user")
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
    public ResponseEntity<ApiResponse> registerUser(@RequestBody User user) {
        service.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Sikeres regisztráció! A hitelesítő email elküldve!"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse httpResponse) {
        LoginResponse response = authService.login(loginRequest);

        Cookie refreshTokenCookie = new Cookie("refreshToken", response.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false); // ha deploy akkor true
        refreshTokenCookie.setMaxAge(60 * 60 * 24 * 30);
        refreshTokenCookie.setPath("/");

        httpResponse.addCookie(refreshTokenCookie);

        System.out.println(response.getRefreshToken());

        response.setRefreshToken(null);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/sendForgottenPassEmail")
    public ResponseEntity<ApiResponse> sendForgottenPassEmail(@RequestBody EmailRequest request) {
        emailService.SendPasswordResetEmail(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success("Email elküldve"));
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody PasswordResetRequest request) {
        service.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success("Sikeres jelszóvisszaállítás"));
    }

    @GetMapping("/verify/{token}")
    public ResponseEntity<ApiResponse> verifyToken(@PathVariable String token) {
        service.verifyUser(token);
        return ResponseEntity.ok(ApiResponse.success("Sikeres aktiválás"));
    }

    @GetMapping("requests/getAllTrainer")
    public ResponseEntity<List<Trainers>> getAllTrainer(){
        return ResponseEntity.ok(service.getAllTrainer());
    }

    @GetMapping("/requests/userById/{id}")
    public ResponseEntity<UserProjection> getUserById(@PathVariable int id) {
        return ResponseEntity.ok(service.getUserById(id));
    }

    @GetMapping("/requests/getAllUser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserProjection>> getAllUserForAdmin() {
        return ResponseEntity.ok(service.getAllUsers());
    }

    @PostMapping("/change/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> changeRole(@RequestBody changeUserRole changeUser) {
        service.changeUserRole(changeUser);
        return ResponseEntity.ok(ApiResponse.success("Szerepkör módosítva"));
    }

    @DeleteMapping("/delete/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable String email) {
        service.deleteUser(email);
        return ResponseEntity.noContent().build();
    }

    @Setter
    @Getter
    public static class LoginRequest {
        private String email;
        private String password;
        private String deviceInfo;

        public LoginRequest(String email, String password, String deviceInfo) {
            this.email = email;
            this.password = password;
            this.deviceInfo = deviceInfo;
        }

    }

    @Setter
    @Getter
    public static class LoginResponse {
        private String accessToken;
        private String refreshToken;

        public LoginResponse(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
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

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Trainers {
        private int trainerId;
        private String trainerName;
    }

}
