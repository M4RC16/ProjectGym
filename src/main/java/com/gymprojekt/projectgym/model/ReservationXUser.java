package com.gymprojekt.projectgym.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Setter
@Getter
@Entity
@Table(name = "reservation_x_user", uniqueConstraints = @UniqueConstraint(columnNames = "trainer_id"), indexes = {
        @Index(name = "user_id", columnList = "user_id"),
        @Index(name = "reservation_id", columnList = "reservation_id"),
        @Index(name = "trainer_id", columnList = "trainer_id")})
public class ReservationXUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_x_user_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "trainer_id")
    private User trainer;

}