package com.projectgym.service;

import com.projectgym.controller.UserController;
import com.projectgym.exception.NotAllowedException;
import com.projectgym.exception.NotFoundException;
import com.projectgym.model.RefreshToken;
import com.projectgym.model.User;
import com.projectgym.repository.UserRepository;
import com.projectgym.security.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    public UserController.LoginResponse login(UserController.LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Hibás email vagy jelszó"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Hibás email vagy jelszó");
        }
        if (!user.isVerified()) {
            throw new NotAllowedException("A fiók nincs hitelesítve");
        }

        String deviceInfo = request.getDeviceInfo() != null
                ? request.getDeviceInfo()
                : "Unknown Device";
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user, deviceInfo);

        String accessToken = jwtService.generateToken(user);

        return new UserController.LoginResponse(
                accessToken,
                refreshToken.getToken());
    }

    public String changePassword(String email, UserController.ResetPasswordRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Felhasználó nem található ID: " + email));

        String oldPass = request.getOldPassword();
        String pass1 = request.getPassword1();
        String pass2 = request.getPassword2();

        if (!passwordEncoder.matches(oldPass, user.getPassword())) {
            throw new BadCredentialsException("Hibás email vagy jelszó");
        }
        if (!Objects.equals(pass1, pass2)){
            throw new BadCredentialsException("A 2 jelszó nem egyezik");
        }

        user.setPassword(passwordEncoder.encode(pass1));
        userRepository.save(user);
        return "Sikeres jelszó átállítás!";
    }
}
