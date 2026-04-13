package com.projectgym.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "stripe_purchase_validation", indexes = {
        @Index(name = "user_id", columnList = "user_id"),
        @Index(name = "ticket_id", columnList = "ticket_id")
})
public class StripePurchaseValidation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @Column(name = "successful_purchase", nullable = false)
    private Boolean successfulPurchase = false;

    @Column(name = "purchase_date", nullable = false)
    private Instant purchaseDate;

}