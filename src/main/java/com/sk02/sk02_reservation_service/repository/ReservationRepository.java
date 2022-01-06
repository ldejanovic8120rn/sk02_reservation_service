package com.sk02.sk02_reservation_service.repository;

import com.sk02.sk02_reservation_service.domain.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Page<Reservation> findReservationByHotel_Name(String hotelName, Pageable pageable);
    Page<Reservation> findReservationByUsername(String username, Pageable pageable);
    List<Reservation> findReservationByStartDateBetweenAndSent(Date startDate, Date endDate, boolean sent);
}
