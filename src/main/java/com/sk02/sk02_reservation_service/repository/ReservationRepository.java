package com.sk02.sk02_reservation_service.repository;

import com.sk02.sk02_reservation_service.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findReservationByHotel_Name(String hotelName);
    List<Reservation> findReservationByUsername(String username);
}
