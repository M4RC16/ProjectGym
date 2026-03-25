package com.projectgym.controller;

import com.projectgym.ApiResponse;
import com.projectgym.model.ContactForm;
import com.projectgym.service.ContactFromService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/form")
@RestController
public class ContactFormController {
    private final ContactFromService service;

    public ContactFormController(ContactFromService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> Form(@RequestBody FormInfo form) {
        service.saveForm(form);
        return ResponseEntity.ok(ApiResponse.success("Sikeres küldés"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteForm(@PathVariable int id) {
        return ResponseEntity.ok(ApiResponse.success(service.deleteForm(id)));
    }

    @GetMapping("/get")
    public ResponseEntity<List<ContactForm>> getContactForm() {
        return ResponseEntity.ok(service.getAllForms());
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FormInfo {
        private String lastName;
        private String firstName;
        private String emailAddress;
        private String phoneNumber;
        private String message;
    }
}
