package com.gymprojekt.forevergym.service;

import com.gymprojekt.forevergym.controller.UserController;
import com.gymprojekt.forevergym.model.User;
import com.gymprojekt.forevergym.repository.UserRepository;
import com.gymprojekt.forevergym.security.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UserController.LoginResponse login(UserController.LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Hibás email vagy jelszó"));

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Hibás email vagy jelszó");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new UserController.LoginResponse(token, user.getEmail(), "Sikeres bejelentkezés");
    }

}
