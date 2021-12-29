package com.sk02.sk02_reservation_service.repository;

import com.sk02.sk02_reservation_service.domain.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findHotelByName(String name);
    List<Hotel> findHotelByCity(String city);
    List<Hotel> findHotelByCityAndName(String city, String name);

}
