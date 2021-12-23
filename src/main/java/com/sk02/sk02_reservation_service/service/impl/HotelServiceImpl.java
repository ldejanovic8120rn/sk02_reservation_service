package com.sk02.sk02_reservation_service.service.impl;

import com.sk02.sk02_reservation_service.domain.Hotel;
import com.sk02.sk02_reservation_service.dto.hotel.HotelCreateDto;
import com.sk02.sk02_reservation_service.dto.hotel.HotelDto;
import com.sk02.sk02_reservation_service.dto.hotel.HotelUpdateDto;
import com.sk02.sk02_reservation_service.exception.NotFoundException;
import com.sk02.sk02_reservation_service.mapper.HotelMapper;
import com.sk02.sk02_reservation_service.repository.HotelRepository;
import com.sk02.sk02_reservation_service.service.HotelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class HotelServiceImpl implements HotelService {

    private static final String hotelNotFound = "Hotel with given id not found!";

    private final HotelMapper hotelMapper;
    private final HotelRepository hotelRepository;

    public HotelServiceImpl(HotelMapper hotelMapper, HotelRepository hotelRepository) {
        this.hotelMapper = hotelMapper;
        this.hotelRepository = hotelRepository;
    }

    @Override
    public HotelDto createHotel(HotelCreateDto hotelCreateDto) {
        Hotel hotel = hotelMapper.hotelCreateDtoToHotel(hotelCreateDto);
        return hotelMapper.hotelToHotelDto(hotelRepository.save(hotel));
    }

    @Override
    public HotelDto updateHotel(Long id, HotelUpdateDto hotelUpdateDto) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new NotFoundException(hotelNotFound));
        hotelMapper.updateHotel(hotel, hotelUpdateDto);

        return hotelMapper.hotelToHotelDto(hotelRepository.save(hotel));
    }

    @Override
    public void deleteHotel(Long id) {
        hotelRepository.deleteById(id);
    }
}
