package com.gymprojekt.projectgym.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Boolean getSuccessfulPurchase() {
        return successfulPurchase;
    }

    public void setSuccessfulPurchase(Boolean successfulPurchase) {
        this.successfulPurchase = successfulPurchase;
    }

    public Instant getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Instant purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

}