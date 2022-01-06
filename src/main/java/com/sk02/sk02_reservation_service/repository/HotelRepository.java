package com.sk02.sk02_reservation_service.repository;

import com.sk02.sk02_reservation_service.domain.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    Page<Hotel> findHotelByName(String name, Pageable pageable);
    Page<Hotel> findHotelByCity(String city, Pageable pageable);
    Page<Hotel> findHotelByCityAndName(String city, String name, Pageable pageable);

}
