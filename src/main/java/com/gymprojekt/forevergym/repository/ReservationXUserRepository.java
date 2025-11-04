package com.gymprojekt.forevergym.repository;

import com.gymprojekt.forevergym.model.Reservation;
import com.gymprojekt.forevergym.model.ReservationXUser;
import com.gymprojekt.forevergym.model.User;
import org.springframework.data.repository.CrudRepository;

import java.time.Instant;
import java.util.List;

public interface ReservationXUserRepository extends CrudRepository<ReservationXUser, Long> {

    List<ReservationXUser> findByUser (User user);

    List<ReservationXUser> findByReservation (Reservation reservation);

    List<ReservationXUser> findByUserId (Integer userId);

    boolean  existsByUserAndReservation (User user, Reservation reservation);

    long countByReservation (Reservation reservation);
}
