package com.sk02.sk02_reservation_service.mapper;

import com.sk02.sk02_reservation_service.domain.Hotel;
import com.sk02.sk02_reservation_service.domain.RoomType;
import com.sk02.sk02_reservation_service.dto.hotel.HotelCreateDto;
import com.sk02.sk02_reservation_service.dto.hotel.HotelDto;
import com.sk02.sk02_reservation_service.dto.hotel.HotelFilterViewDto;
import com.sk02.sk02_reservation_service.dto.hotel.HotelUpdateDto;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class HotelMapper {

    public HotelDto hotelToHotelDto(Hotel hotel){
        HotelDto hotelDto = new HotelDto();

        hotelDto.setId(hotel.getId());
        hotelDto.setName(hotel.getName());
        hotelDto.setDescription(hotel.getDescription());
        hotelDto.setCity(hotel.getCity());

        return hotelDto;
    }

    public Hotel hotelCreateDtoToHotel(HotelCreateDto hotelCreateDto){
        Hotel hotel = new Hotel();

        hotel.setName(hotelCreateDto.getName());
        hotel.setDescription(hotelCreateDto.getDescription());
        hotel.setCity(hotelCreateDto.getCity());

        return hotel;
    }

    public void updateHotel(Hotel hotel, HotelUpdateDto hotelUpdateDto){
        if (hotelUpdateDto.getName() != null){
            hotel.setName(hotelUpdateDto.getName());
        }
        if (hotelUpdateDto.getDescription() != null){
            hotel.setDescription(hotelUpdateDto.getDescription());
        }
        if (hotelUpdateDto.getCity() != null){
            hotel.setCity(hotelUpdateDto.getCity());
        }
    }

    public HotelFilterViewDto makeFilterView(Hotel hotel, RoomType roomType, Date startDate, Date endDate){
        HotelFilterViewDto filterViewDto = new HotelFilterViewDto();

        filterViewDto.setHotelId(hotel.getId().toString());
        filterViewDto.setHotelName(hotel.getName());
        filterViewDto.setHotelDescription(hotel.getDescription());
        filterViewDto.setHotelCity(hotel.getCity());

        filterViewDto.setRoomTypeId(roomType.getId().toString());
        filterViewDto.setRoomTypeCategory(roomType.getCategory());
        filterViewDto.setRoomTypePrice(String.valueOf(roomType.getPrice()));

        filterViewDto.setStartDate(startDate);
        filterViewDto.setEndDate(endDate);

        return filterViewDto;
    }

}
