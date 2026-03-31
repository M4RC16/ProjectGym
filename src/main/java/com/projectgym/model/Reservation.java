package com.projectgym.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "scheduled_at", nullable = false)
    private LocalDateTime scheduledAt;

    @CreationTimestamp
    @Column(name = "reservation_date", nullable = false)
    private LocalDateTime reservationDate;
}