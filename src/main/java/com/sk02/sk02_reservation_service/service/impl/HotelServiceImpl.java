package com.sk02.sk02_reservation_service.service.impl;

import com.sk02.sk02_reservation_service.domain.Hotel;
import com.sk02.sk02_reservation_service.dto.hotel.HotelCreateDto;
import com.sk02.sk02_reservation_service.dto.hotel.HotelDto;
import com.sk02.sk02_reservation_service.dto.hotel.HotelUpdateDto;
import com.sk02.sk02_reservation_service.dto.user.HotelNewNameDto;
import com.sk02.sk02_reservation_service.dto.user.ManagerAttributesDto;
import com.sk02.sk02_reservation_service.exception.NotFoundException;
import com.sk02.sk02_reservation_service.mapper.HotelMapper;
import com.sk02.sk02_reservation_service.repository.HotelRepository;
import com.sk02.sk02_reservation_service.service.HotelService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class HotelServiceImpl implements HotelService {

    private static final String hotelNotFound = "Hotel with given id not found!";

    private final HotelMapper hotelMapper;
    private final HotelRepository hotelRepository;
    private final RestTemplate userServiceRestTemplate;

    public HotelServiceImpl(HotelMapper hotelMapper, HotelRepository hotelRepository, RestTemplate userServiceRestTemplate) {
        this.hotelMapper = hotelMapper;
        this.hotelRepository = hotelRepository;
        this.userServiceRestTemplate = userServiceRestTemplate;
    }

    @Override
    public HotelDto getHotelById(Long id) {
        return hotelMapper.hotelToHotelDto(hotelRepository.findById(id).orElseThrow(() -> new NotFoundException(hotelNotFound)));
    }

    @Override
    public HotelDto createHotel(HotelCreateDto hotelCreateDto) {
        Hotel hotel = hotelMapper.hotelCreateDtoToHotel(hotelCreateDto);
        return hotelMapper.hotelToHotelDto(hotelRepository.save(hotel));
    }

    @Override
    public HotelDto updateHotel(Long id, HotelUpdateDto hotelUpdateDto) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new NotFoundException(hotelNotFound));

        if(hotelUpdateDto.getName() != null){
            HotelNewNameDto hotelNewNameDto = new HotelNewNameDto();
            hotelNewNameDto.setOldName(hotel.getName());
            hotelNewNameDto.setNewName(hotelUpdateDto.getName());

            userServiceRestTemplate.exchange("/manager-attributes/new-name", HttpMethod.PUT, new HttpEntity<>(hotelNewNameDto), ManagerAttributesDto.class);
        }

        hotelMapper.updateHotel(hotel, hotelUpdateDto);

        return hotelMapper.hotelToHotelDto(hotelRepository.save(hotel));
    }

    @Override
    public void deleteHotel(Long id) {
        hotelRepository.deleteById(id);
    }

    @Override
    public List<HotelDto> getAllHotels() {
        return hotelRepository.findAll().stream().map(hotelMapper::hotelToHotelDto).collect(Collectors.toList());
    }
}
