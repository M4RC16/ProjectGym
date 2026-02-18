package com.gymprojekt.forevergym.controller;

import com.gymprojekt.forevergym.ApiResponse;
import com.gymprojekt.forevergym.model.RefreshToken;
import com.gymprojekt.forevergym.model.User;
import com.gymprojekt.forevergym.security.JwtService;
import com.gymprojekt.forevergym.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<ApiResponse> logout(@RequestBody LogoutRequest request) {
        RefreshToken refreshToken = refreshTokenService.findByToken(request.getRefreshToken());
        refreshTokenService.deleteById(refreshToken.getId());

        return ResponseEntity.ok(ApiResponse.success("Sikeres kijelentkezés"));
    }

    @PostMapping("/logout/all")
    public ResponseEntity<ApiResponse> logoutAll(@RequestBody LogoutRequest request) {
        RefreshToken refreshToken = refreshTokenService.findByToken(request.getRefreshToken());
        User user = refreshToken.getUser();
        refreshTokenService.deleteByUser(user);
        return ResponseEntity.ok(ApiResponse.success("Kijelentkeztél minden eszközről"));
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokenRefreshRequest {
        private String refreshToken;
    }

    @Getter
    @AllArgsConstructor
    public static class TokenRefreshResponse {
        private String accessToken;
        private String refreshToken;
        private String message;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LogoutRequest {
        private String refreshToken;
    }
}
