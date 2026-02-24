package com.gymprojekt.projectgym.repository;

import com.gymprojekt.projectgym.model.Reservation;
import com.gymprojekt.projectgym.model.ReservationXUser;
import com.gymprojekt.projectgym.model.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationXUserRepository extends CrudRepository<ReservationXUser, Long> {

    @Query("""
    SELECT r.scheduledAt
    FROM ReservationXUser rxu
    JOIN rxu.reservation r
    WHERE rxu.trainer.id = :trainerId
    AND r.scheduledAt >= :startDate
    AND r.scheduledAt < :endDate
    """)
    List<LocalDateTime> findBookedSlotsByTrainerAndDateRange(@Param("trainerId") int trainerId,@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate
    );

    List<ReservationXUser> findByUser (User user);

    List<ReservationXUser> findByReservation (Reservation reservation);

    List<ReservationXUser> findByUserId (Integer userId);

    boolean  existsByUserAndReservation (User user, Reservation reservation);

    long countByReservation (Reservation reservation);

    List<ReservationXUser> findByTrainerId (Integer userId);

    List<ReservationXUser> findByTrainer (User user);

    boolean existsByTrainerIdAndReservationScheduledAt(int trainerId, LocalDateTime scheduledAt);
}
