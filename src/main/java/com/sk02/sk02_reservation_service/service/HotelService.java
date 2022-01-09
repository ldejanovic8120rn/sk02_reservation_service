package com.sk02.sk02_reservation_service.service;

import com.sk02.sk02_reservation_service.dto.hotel.HotelCreateDto;
import com.sk02.sk02_reservation_service.dto.hotel.HotelDto;
import com.sk02.sk02_reservation_service.dto.hotel.HotelUpdateDto;

import java.util.List;

public interface HotelService {

    HotelDto getHotelById(Long id);

    HotelDto createHotel(HotelCreateDto hotelCreateDto);

    HotelDto updateHotel(Long id, HotelUpdateDto hotelUpdateDto);

    void deleteHotel(Long id);

    List<HotelDto> getAllHotels();

    HotelDto getHotelByManager(String authorization);
}
