package com.sk02.sk02_reservation_service.service;

import com.sk02.sk02_reservation_service.dto.hotel.HotelFilterDto;
import com.sk02.sk02_reservation_service.dto.hotel.HotelFilterViewDto;

import java.util.List;

public interface HotelFilterService {

    List<HotelFilterViewDto> findHotels(HotelFilterDto hotelFilterDto);
    //TODO page ili list?
}
