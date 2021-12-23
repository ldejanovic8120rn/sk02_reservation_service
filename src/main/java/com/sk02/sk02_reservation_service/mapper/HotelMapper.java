package com.sk02.sk02_reservation_service.mapper;

import com.sk02.sk02_reservation_service.domain.Hotel;
import com.sk02.sk02_reservation_service.dto.hotel.HotelCreateDto;
import com.sk02.sk02_reservation_service.dto.hotel.HotelDto;
import com.sk02.sk02_reservation_service.dto.hotel.HotelUpdateDto;
import org.springframework.stereotype.Component;

@Component
public class HotelMapper {

    public HotelDto hotelToHotelDto(Hotel hotel){
        HotelDto hotelDto = new HotelDto();

        hotelDto.setId(hotel.getId());
        hotelDto.setName(hotel.getName());
        hotelDto.setDescription(hotel.getDescription());
        hotelDto.setRoomsNumber(hotel.getRoomsNumber());

        return hotelDto;
    }

    public Hotel hotelCreateDtoToHotel(HotelCreateDto hotelCreateDto){
        Hotel hotel = new Hotel();

        hotel.setName(hotelCreateDto.getName());
        hotel.setDescription(hotelCreateDto.getDescription());
        hotel.setRoomsNumber(0);

        return hotel;
    }

    public void updateHotel(Hotel hotel, HotelUpdateDto hotelUpdateDto){
        if (hotelUpdateDto.getName() != null){
            hotel.setName(hotelUpdateDto.getName());
        }
        if (hotelUpdateDto.getDescription() != null){
            hotel.setDescription(hotelUpdateDto.getDescription());
        }
    }

}
