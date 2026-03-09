package com.projectgym.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "ticket")
public class Ticket {
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

    @Column(name = "unit", nullable = false)
    private String unit;

}