package com.gymprojekt.forevergym.service;

import com.gymprojekt.forevergym.controller.UserController;
import com.gymprojekt.forevergym.exception.EmailIsNotValidException;
import com.gymprojekt.forevergym.exception.PasswordProblemException;
import com.gymprojekt.forevergym.exception.UserAlreadyExistsException;
import com.gymprojekt.forevergym.model.User;
import com.gymprojekt.forevergym.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserService {

    String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    String passwordRegex = "^(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Lazy
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public String registerUser(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Az email regisztrálva van!");
        }
        if (!user.getEmail().matches(emailRegex)) {
            throw new EmailIsNotValidException("Az emailcím amit beítrál nem felel meg!");
        }
        if (!user.getPassword().matches(passwordRegex)) {
            throw new PasswordProblemException("A jelszó legalább 8 karakter hosszú, legalább 1 számot és 1 speciális karaktert kell tartalmazzon!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setVerified(false);
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        user.setTokenExpiryDate(LocalDateTime.now().plusHours(12));

        emailService.SendVerificationEmail(user.getEmail(), token);
        userRepository.save(user);

        return "Sikeres regisztráció! A hitelesítő email elküldve!";
    }

    public String verifyUser(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("A felhasználó nem található"));

        if (user.getTokenExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("A token lejárt, kérj új emailt");
        }

        user.setVerified(true);
        user.setVerificationToken(null);
        user.setTokenExpiryDate(null);
        userRepository.save(user);

        return "Sikeres aktiválás";
    }

    private String generateAlphanumericCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

    public String sendToken(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("A felhasználó nem található"));

        if (user.getResetTokenExpiryDate() != null && user.getResetTokenExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("A kért token még érvényes, nem küldök újat.");
        }

        String resetToken = generateAlphanumericCode();
        user.setResetPasswordToken(resetToken);
        user.setResetTokenExpiryDate(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);

        return resetToken;
    }

    public String resetPassword(UserController.PasswordResetRequest request) {

        String password;
        if (!Objects.equals(request.getPassword1(), request.getPassword2())) {
            throw new RuntimeException("A kettő beírt jelszó nem egyezik meg!");
        } else {
            password = request.getPassword1();
        }

        if (!password.matches(passwordRegex)) {
            throw new RuntimeException("A jelszó legalább 8 karakter hosszú, legalább 1 számot és 1 speciális karaktert kell tartalmazzon!");
        }

        User user = userRepository.findByResetPasswordToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("A felhasználó nem található"));

        if (user.getResetTokenExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("A token lejárt, kérj új emailt");
        }

        user.setPassword(passwordEncoder.encode(password));
        user.setResetPasswordToken(null);
        user.setResetTokenExpiryDate(null);
        userRepository.save(user);

        return "Sikeres jelszóvisszaállítás";
    }

}