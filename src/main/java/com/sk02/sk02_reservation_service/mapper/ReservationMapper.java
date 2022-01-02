package com.sk02.sk02_reservation_service.mapper;

import com.sk02.sk02_reservation_service.domain.Reservation;
import com.sk02.sk02_reservation_service.dto.reservation.ReservationCreateDto;
import com.sk02.sk02_reservation_service.dto.reservation.ReservationDto;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    public ReservationDto reservationToReservationDto(Reservation reservation){
        ReservationDto reservationDto = new ReservationDto();

        reservationDto.setId(reservation.getId());
        reservationDto.setRoomTypeId(reservation.getRoom().getRoomType().getId());
        reservationDto.setUsername(reservation.getUsername());
        reservationDto.setPrice(reservation.getPrice());
        reservationDto.setStartDate(reservation.getStartDate());
        reservationDto.setEndDate(reservation.getEndDate());

        return reservationDto;
    }

    public Reservation reservationCreateDtoToReservation(ReservationCreateDto reservationCreateDto){
        Reservation reservation = new Reservation();

        reservation.setStartDate(reservationCreateDto.getStartDate());
        reservation.setEndDate(reservationCreateDto.getEndDate());

        return reservation;
    }
}
