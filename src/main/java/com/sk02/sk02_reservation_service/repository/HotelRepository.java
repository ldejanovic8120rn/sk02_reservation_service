package com.sk02.sk02_reservation_service.repository;

import com.sk02.sk02_reservation_service.domain.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
}
