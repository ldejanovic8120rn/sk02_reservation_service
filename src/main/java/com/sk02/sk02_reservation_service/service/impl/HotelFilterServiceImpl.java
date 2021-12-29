package com.sk02.sk02_reservation_service.service.impl;

import com.sk02.sk02_reservation_service.domain.Hotel;
import com.sk02.sk02_reservation_service.domain.Room;
import com.sk02.sk02_reservation_service.dto.hotel.HotelFilterDto;
import com.sk02.sk02_reservation_service.dto.hotel.HotelFilterViewDto;
import com.sk02.sk02_reservation_service.exception.DateRequiredException;
import com.sk02.sk02_reservation_service.repository.HotelRepository;
import com.sk02.sk02_reservation_service.service.HotelFilterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class HotelFilterServiceImpl implements HotelFilterService {

    private static final String dateRequiredMessage = "Start date and end date are required";

    private HotelRepository hotelRepository;

    @Override
    public List<HotelFilterViewDto> findHotels(HotelFilterDto hotelFilterDto) {

        if(hotelFilterDto.getStartDate() == null || hotelFilterDto.getEndDate() == null){
            throw new DateRequiredException(dateRequiredMessage);
        }

        List<Hotel> hotels;
        if(hotelFilterDto.getCity() != null && hotelFilterDto.getName() != null){
            hotels = hotelRepository.findHotelByCityAndName(hotelFilterDto.getCity(), hotelFilterDto.getName());
        }
        else if(hotelFilterDto.getName() != null){
            hotels = hotelRepository.findHotelByName(hotelFilterDto.getName());
        }
        else if(hotelFilterDto.getCity() != null){
            hotels = hotelRepository.findHotelByCity(hotelFilterDto.getCity());
        }
        else {
            hotels = hotelRepository.findAll();
        }

        return filterDates(hotels, hotelFilterDto.getStartDate(), hotelFilterDto.getEndDate(), hotelFilterDto.getPriceSort());
    }

    private List<HotelFilterViewDto> filterDates(List<Hotel> hotels, Date startDate, Date endDate, String priceSort){
        List<HotelFilterViewDto> hotelsToReturn = new ArrayList<>();

        for (Hotel hotel: hotels){
            for (Room room: hotel.getRooms()){
                //termin -> roomId
                if(room.getPeriod().isEmpty()){
                    //dodaj u listu
                }
            }
        }

        return hotelsToReturn;
    }
}
