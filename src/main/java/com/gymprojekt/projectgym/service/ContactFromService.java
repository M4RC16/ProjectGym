package com.gymprojekt.projectgym.service;

import com.gymprojekt.projectgym.controller.ContactFormController;
import com.gymprojekt.projectgym.repository.ContactFormRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class ContactFromService {

    private final ContactFormRepository repository;
    public ContactFromService(ContactFormRepository repository) {
        this.repository = repository;
    }

    String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    String phoneRegex = "^[0-9+ ]{7,15}$";

    public String saveForm(ContactFormController.FormInfo form) {
        if (form.getPhoneNumber() != null && !form.getPhoneNumber().matches(phoneRegex)) {
            throw new BadCredentialsException("Érvénytelen telefonszám formátum!");
        }
        if (form.getEmailAddress() != null && !form.getEmailAddress().matches(emailRegex)) {
            throw new BadCredentialsException("Érvénytelen email formátum");
        }
        if (form.getMessage() == null) {
            throw new BadCredentialsException("Nem lehet üres az üzenet mező");
        }
        if (form.getFirstName() == null || form.getLastName() == null) {
            throw new BadCredentialsException("Nem lehet üres a név");
        }

        repository.save(new com.gymprojekt.projectgym.model.ContactForm(
                form.getFirstName() + " " + form.getLastName(),
                form.getPhoneNumber(),
                form.getEmailAddress(),
                form.getMessage()
        ));

        return "Sikeres küldés";
    }

    
}
