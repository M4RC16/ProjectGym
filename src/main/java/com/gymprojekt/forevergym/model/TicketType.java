package com.gymprojekt.forevergym.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ticket_type")
public class TicketType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "ticket_name", nullable = false)
    private String ticketName;

    @Column(name = "validity_length", nullable = false)
    private Integer validityLength;

    @Column(name = "price", nullable = false)
    private Integer price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public Integer getValidityLength() {
        return validityLength;
    }

    public void setValidityLength(Integer validityLength) {
        this.validityLength = validityLength;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

}