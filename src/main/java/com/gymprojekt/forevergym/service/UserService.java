package com.gymprojekt.forevergym.service;

import com.gymprojekt.forevergym.exception.EmailIsNotValidException;
import com.gymprojekt.forevergym.exception.PasswordProblemException;
import com.gymprojekt.forevergym.exception.UserAlreadyExistsException;
import com.gymprojekt.forevergym.model.User;
import com.gymprojekt.forevergym.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    String passwordRegex = "^(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) {

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
        return userRepository.save(user);
    }

}
