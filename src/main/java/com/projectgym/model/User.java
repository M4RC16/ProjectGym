package com.projectgym.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Setter
@Getter
@Entity
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(name = "email", columnNames = {"email"})
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "birthdate")
    private LocalDate birthdate;

    @Column(name = "phone_number", length = 50)
    private String phoneNumber;

    @Lob
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "sex", length = 50)
    private String sex;

    @Column(name = "valid_until")
    private LocalDate validUntil;

    @Column(name = "has_valid_ticket")
    private Boolean hasValidTicket;

    @Column(name = "hourly_wage")
    private Integer hourlyWage;

    @Column(name = "start_shift")
    private LocalTime startShift;

    @Column(name = "end_shift")
    private LocalTime endShift;

    @Column(name = "isVerified", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean isVerified = false;

    @Column(name = "verificationToken")
    private String verificationToken;

    @Column(name = "tokenExpiryDate")
    private LocalDateTime tokenExpiryDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @Column(name = "resetTokenExpiryDate")
    private LocalDateTime resetTokenExpiryDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", columnDefinition = "INTEGER DEFAULT 3")
    private Role role;

}