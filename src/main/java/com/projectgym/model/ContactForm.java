package com.projectgym.model;

import jakarta.persistence.*;

@Entity
@Table(name = "contact_form")
public class ContactForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "message", nullable = false)
    private String message;

    public ContactForm(String name, String phoneNumber, String emailAddress, String message) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = emailAddress;
        this.message = message;
    }
}
