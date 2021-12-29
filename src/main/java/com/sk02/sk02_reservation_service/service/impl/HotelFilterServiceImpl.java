package com.sk02.sk02_reservation_service.service.impl;

import com.sk02.sk02_reservation_service.dto.hotel.HotelFilterDto;
import com.sk02.sk02_reservation_service.dto.hotel.HotelFilterViewDto;
import com.sk02.sk02_reservation_service.repository.HotelRepository;
import com.sk02.sk02_reservation_service.service.HotelFilterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HotelFilterServiceImpl implements HotelFilterService {

    private HotelRepository hotelRepository;

    @Override
    public List<HotelFilterViewDto> findHotels(HotelFilterDto hotelFilterDto) {
        return null;
    }
}
