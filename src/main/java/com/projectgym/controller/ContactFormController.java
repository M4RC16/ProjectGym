package com.projectgym.controller;

import com.projectgym.service.ContactFromService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/form")
@RestController
public class ContactFormController {
    private final ContactFromService service;

    public ContactFormController(ContactFromService service) {
        this.service = service;
    }

    @PostMapping("/ContactForm")
    public ResponseEntity<?> Form(@RequestBody FormInfo form) {
        String result = service.saveForm(form);
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
