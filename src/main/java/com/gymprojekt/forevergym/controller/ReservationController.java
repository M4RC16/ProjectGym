package com.gymprojekt.forevergym.controller;

import com.gymprojekt.forevergym.ApiResponse;
import com.gymprojekt.forevergym.service.ReservationService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/getMyTrainings")
    public ResponseEntity<List<ReservationService.TrainingResponse>> getMyTrainings(@CookieValue(value = "refreshToken") String refreshToken) {
        List<ReservationService.TrainingResponse> myTrainings = reservationService.getMyTrainings(refreshToken);
        return ResponseEntity.ok(myTrainings);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteTraining(@RequestBody ReservationService.TrainingResponse reservation) {
        int trainerId = reservation.getTrainerId();
        int reservationId = reservation.getReservationId();
        int reservationXUserId = reservation.getReservationXUserId();

        reservationService.deleteReservation(reservationXUserId, reservationId, trainerId);
        return ResponseEntity.ok(ApiResponse.success("Foglalás sikeresen törölve"));
    }

    @PostMapping("/new/training")
    public ResponseEntity<ApiResponse> newTraining(@CookieValue(value = "refreshToken") String refreshToken, @RequestBody int trainerId) {
        reservationService.newReservation(refreshToken, trainerId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Sikeres foglalás!"));
    }
}