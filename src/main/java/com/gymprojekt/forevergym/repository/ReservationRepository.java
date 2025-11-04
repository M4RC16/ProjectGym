package com.gymprojekt.forevergym.repository;

import com.gymprojekt.forevergym.model.Reservation;
import org.springframework.data.repository.CrudRepository;

import java.time.Instant;
import java.util.List;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    List<Reservation> findByScheduledAtBetween(Integer start, Instant end);

    List<Reservation> findByReservationDateAfter(Instant date);

    List<Reservation> findByReservationDateBefore(Instant date);

}
