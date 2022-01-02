package com.sk02.sk02_reservation_service.controller;

import com.sk02.sk02_reservation_service.dto.reservation.ReservationCreateDto;
import com.sk02.sk02_reservation_service.dto.reservation.ReservationDto;
import com.sk02.sk02_reservation_service.security.CheckHotelManager;
import com.sk02.sk02_reservation_service.security.CheckSecurity;
import com.sk02.sk02_reservation_service.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/manager")
    @CheckSecurity(roles = {"MANAGER"})
    public ResponseEntity<List<ReservationDto>> getReservationsByHotel(@RequestHeader("Authorization") String authorization){
        return new ResponseEntity<>(reservationService.getReservationsByHotel(authorization), HttpStatus.OK);
    }

    @GetMapping
    @CheckSecurity(roles = {"CLIENT"})
    public ResponseEntity<List<ReservationDto>> getReservations(@RequestHeader("Authorization") String authorization){
        return new ResponseEntity<>(reservationService.getReservations(authorization), HttpStatus.OK);
    }

    @PostMapping
    @CheckSecurity(roles = {"CLIENT"})
    public ResponseEntity<ReservationDto> makeReservation(@RequestHeader("Authorization") String authorization, @RequestBody ReservationCreateDto reservationCreateDto){
        return new ResponseEntity<>(reservationService.makeReservation(reservationCreateDto, authorization), HttpStatus.CREATED);
    }

    @DeleteMapping("/manager/{id}")
    @CheckSecurity(roles = {"MANAGER"})
    public ResponseEntity<HttpStatus> deleteReservationManager(@RequestHeader("Authorization") String authorization, @PathVariable("id") Long id){
        reservationService.deleteReservationManager(id, authorization);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @CheckSecurity(roles = {"CLIENT"})
    public ResponseEntity<HttpStatus> deleteReservation(@RequestHeader("Authorization") String authorization, @PathVariable("id") Long id){
        reservationService.deleteReservation(id, authorization);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
