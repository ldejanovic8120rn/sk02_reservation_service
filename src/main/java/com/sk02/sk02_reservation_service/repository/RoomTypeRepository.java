package com.sk02.sk02_reservation_service.repository;

import com.sk02.sk02_reservation_service.domain.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {

    List<RoomType> findAllByHotelId(Long hotelId);
}
