package com.sk02.sk02_reservation_service.repository;

import com.sk02.sk02_reservation_service.domain.Review;
import com.sk02.sk02_reservation_service.dto.hotel.BestHotelDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query(value = "SELECT r.hotel_id as hotelId, h.name as hotelName, h.city as city, AVG(rate) as rate from reviews r JOIN hotels h ON r.hotel_id=h.id group by hotel_id, h.name, h.city order by AVG(rate) asc limit 3", nativeQuery = true)
    List<BestHotelDto> bestHotelsList();
}
