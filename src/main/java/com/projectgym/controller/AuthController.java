package com.projectgym.controller;

import com.projectgym.ApiResponse;
import com.projectgym.model.RefreshToken;
import com.projectgym.model.User;
import com.projectgym.security.JwtService;
import com.projectgym.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@CookieValue(value = "refreshToken") String refreshToken) {

        RefreshToken token = refreshTokenService.findByToken(refreshToken);
        refreshTokenService.verifyExpiration(token);

        User user = token.getUser();
        String accessToken = jwtService.generateToken(user);

        return ResponseEntity.ok(new TokenRefreshResponse(accessToken, refreshToken, "Access token frissítve"));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@CookieValue(value = "refreshToken") String request) {
        RefreshToken refreshToken = refreshTokenService.findByToken(request);
        refreshTokenService.deleteById(refreshToken.getId());

        return ResponseEntity.ok(ApiResponse.success("Sikeres kijelentkezés"));
    }

    @PostMapping("/logout/all")
    public ResponseEntity<ApiResponse> logoutAll(@CookieValue(value = "refreshToken") String request) {
        RefreshToken refreshToken = refreshTokenService.findByToken(request);
        User user = refreshToken.getUser();
        refreshTokenService.deleteByUser(user);
        return ResponseEntity.ok(ApiResponse.success("Kijelentkeztél minden eszközről"));
    }

    @Getter
    @AllArgsConstructor
    public static class TokenRefreshResponse {
        private String accessToken;
        private String refreshToken;
        private String message;
    }
}
