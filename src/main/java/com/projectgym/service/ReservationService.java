package com.projectgym.service;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projectgym.exception.AlreadyExistsException;
import com.projectgym.model.Reservation;
import com.projectgym.exception.NotFoundException;
import com.projectgym.model.RefreshToken;
import com.projectgym.model.ReservationXUser;
import com.projectgym.model.User;
import com.projectgym.repository.RefreshTokenRepository;
import com.projectgym.repository.ReservationRepository;
import com.projectgym.repository.ReservationXUserRepository;
import com.projectgym.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationXUserRepository reservationXUserRepository;
    private final ReservationRepository reservationRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public ReservationService(ReservationXUserRepository reservationXUserRepository,
                              ReservationRepository reservationRepository,
                              RefreshTokenRepository refreshTokenRepository,
                              UserRepository userRepository,
                              EmailService emailService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.reservationXUserRepository = reservationXUserRepository;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public List<TrainingResponse> getMyTrainings(String refreshToken) {

        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("RefreshToken nem található"));

        int userId = token.getUser().getId();

        List<ReservationXUser> reservations = reservationXUserRepository.findByUserId(userId);

        return reservations.stream()
                .map(rxu -> new TrainingResponse(
                        rxu.getId(),
                        rxu.getReservation().getId(),
                        rxu.getReservation().getScheduledAt(),
                        rxu.getTrainer().getFirstName() + " " + rxu.getTrainer().getLastName(),
                        rxu.getTrainer().getId()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteReservation(int reservationXUserId, int reservationId, int trainerId) {

        ReservationXUser reservationXUser = reservationXUserRepository.findById((long) reservationXUserId)
                .orElseThrow(() -> new NotFoundException("Nincs ilyen foglalás: " + reservationXUserId));

        LocalDateTime scheduledAt = reservationXUser.getReservation().getScheduledAt();

        User trainer = userRepository.findById(trainerId)
                .orElseThrow(() -> new NotFoundException("Nincs ilyen edző: " + trainerId));

        String trainerEmail = trainer.getEmail();
        emailService.SendDeleteReservation(trainerEmail, scheduledAt);

        reservationXUserRepository.deleteById((long) reservationXUserId);
        reservationRepository.deleteById(reservationId);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void newReservation(String refreshToken, int trainerId, LocalDateTime scheduledAt) {

        boolean alreadyBooked = reservationXUserRepository
                .existsByTrainerIdAndReservationScheduledAt(trainerId, scheduledAt);

        if (alreadyBooked) {
            throw new AlreadyExistsException("Ez az időpont már foglalt!");
        }

        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("RefreshToken nem található"));

        User user = token.getUser();

        User trainer = userRepository.findById(trainerId)
                .orElseThrow(() -> new NotFoundException("Nincs ilyen edző: " + trainerId));

        Reservation reservation = new Reservation();
        reservation.setScheduledAt(scheduledAt);
        Reservation savedReservation = reservationRepository.save(reservation);

        ReservationXUser rxu = new ReservationXUser();
        rxu.setUser(user);
        rxu.setTrainer(trainer);
        rxu.setReservation(savedReservation);
        reservationXUserRepository.save(rxu);

    }

    public List<FreeTrainingResponse> getFreeTrainings(int trainerId, LocalDateTime date) {
        List<LocalDateTime> timeSlots = getTimes(date);


        LocalDateTime dayStart = date.toLocalDate().atStartOfDay();
        LocalDateTime dayEnd = dayStart.plusDays(1);

        List<LocalDateTime> bookedSlots = reservationXUserRepository
                .findBookedSlotsByTrainerAndDateRange(trainerId, dayStart, dayEnd);

        return timeSlots.stream()
                .map(slot -> new FreeTrainingResponse(slot, !bookedSlots.contains(slot)))
                .collect(Collectors.toList());

    }

    private static List<LocalDateTime> getTimes(LocalDateTime date) {
        List<LocalDateTime> timeSlots = new ArrayList<>();

        LocalDateTime current = date.toLocalDate().atTime(10, 0);
        LocalDateTime morningEnd = date.toLocalDate().atTime(13, 0);
        while (!current.isAfter(morningEnd)) {
            timeSlots.add(current);
            current = current.plusMinutes(60);
        }

        current = date.toLocalDate().atTime(14, 30);
        LocalDateTime afternoonEnd = date.toLocalDate().atTime(17, 30);
        while (!current.isAfter(afternoonEnd)) {
            timeSlots.add(current);
            current = current.plusMinutes(60);
        }
        return timeSlots;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TrainingResponse {
        private Integer reservationXUserId;
        private Integer reservationId;
        private LocalDateTime scheduledAt;
        private String trainerName;
        private Integer trainerId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FreeTrainingResponse {
        @JsonFormat(pattern = "HH:mm")
        private LocalDateTime scheduledAt;
        private boolean isFree;
    }

}
