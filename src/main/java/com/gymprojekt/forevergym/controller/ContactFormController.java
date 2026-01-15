package com.gymprojekt.forevergym.controller;

import com.gymprojekt.forevergym.service.EmailService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContactFormController {
    private final EmailService emailService;

    public ContactFormController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/ContactForm")
    public ResponseEntity<?> sendForm(@RequestBody FormInfo form) {
        String result = emailService.SendForm(form);
        return ResponseEntity.ok(result);
    }

    @Setter
    @Getter
    public static class FormInfo {
        private String lastName;
        private String firstName;
        private String emailAddress;
        private String phoneNumber;
        private String message;

        public FormInfo(String lastName, String firstName, String emailAddress, String phoneNumber, String message) {
            this.lastName = lastName;
            this.firstName = firstName;
            this.emailAddress = emailAddress;
            this.phoneNumber = phoneNumber;
            this.message = message;
        }
    }

}
