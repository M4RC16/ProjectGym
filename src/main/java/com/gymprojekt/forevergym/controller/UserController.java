package com.gymprojekt.forevergym.controller;

import com.gymprojekt.forevergym.model.User;
import com.gymprojekt.forevergym.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registerUser(user));
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return "Sikeres bejelentkezés!";
    }

}
