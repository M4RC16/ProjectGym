package com.gymprojekt.forevergym.service;

import com.gymprojekt.forevergym.exception.NotFoundException;
import com.gymprojekt.forevergym.model.RefreshToken;
import com.gymprojekt.forevergym.model.ReservationXUser;
import com.gymprojekt.forevergym.model.User;
import com.gymprojekt.forevergym.repository.RefreshTokenRepository;
import com.gymprojekt.forevergym.repository.ReservationRepository;
import com.gymprojekt.forevergym.repository.ReservationXUserRepository;
import com.gymprojekt.forevergym.repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    public void newReservation(String token, int trainerId) {

    }

    @Getter
    @Setter
    public static class TrainingResponse {
        private Integer reservationXUserId;
        private Integer reservationId;
        private LocalDateTime scheduledAt;
        private String trainerName;
        private Integer trainerId;

        public TrainingResponse(Integer reservationXUserId, Integer reservationId,
                                LocalDateTime scheduledAt, String trainerName, Integer trainerId) {
            this.reservationXUserId = reservationXUserId;
            this.reservationId = reservationId;
            this.scheduledAt = scheduledAt;
            this.trainerName = trainerName;
            this.trainerId = trainerId;
        }
    }

}
