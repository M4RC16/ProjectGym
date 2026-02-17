package com.gymprojekt.forevergym.controller;

import com.gymprojekt.forevergym.ApiResponse;
import com.gymprojekt.forevergym.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/reservation")
public class ReservationController {

    private final ReservationService reservationService;
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/getMyTrainings")
    public ResponseEntity<List<ReservationService.TrainingResponse>> getMyTrainings(@CookieValue(value = "refreshToken") String refreshToken ){
        List<ReservationService.TrainingResponse> myTrainings = reservationService.getMyTrainings(refreshToken);
        return ResponseEntity.ok(myTrainings);
    }

//    @DeleteMapping("/delete")
//    public ResponseEntity<ApiResponse> deleteTraining(ReservationService.TrainingResponse reservation){
//        ReservationService.deleteReservation(reservation);
//        return ResponseEntity.ok(ApiResponse.success("Sikeres törlés!"));
//    }

}
