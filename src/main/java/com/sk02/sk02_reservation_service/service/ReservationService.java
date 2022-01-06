package com.sk02.sk02_reservation_service.service;

import com.sk02.sk02_reservation_service.dto.reservation.ReservationCreateDto;
import com.sk02.sk02_reservation_service.dto.reservation.ReservationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReservationService {

    List<ReservationDto> getReservationsByHotel(String authorization, Pageable pageable);
    List<ReservationDto> getReservations(String authorization, Pageable pageable);
    ReservationDto makeReservation(ReservationCreateDto reservationCreateDto, String authorization);

    void deleteReservationManager(Long id, String authorization);
    void deleteReservation(Long id, String authorization);
}
