package com.sk02.sk02_reservation_service.mapper;

import com.sk02.sk02_reservation_service.domain.RoomType;
import com.sk02.sk02_reservation_service.dto.roomtype.RoomTypeCreateDto;
import com.sk02.sk02_reservation_service.dto.roomtype.RoomTypeDto;
import com.sk02.sk02_reservation_service.dto.roomtype.RoomTypeUpdateDto;
import org.springframework.stereotype.Component;

@Component
public class RoomTypeMapper {

    public RoomTypeDto roomTypeToRoomTypeDto(RoomType roomType){
        RoomTypeDto roomTypeDto = new RoomTypeDto();

        roomTypeDto.setId(roomType.getId());
        roomTypeDto.setCategory(roomType.getCategory());
        roomTypeDto.setPrice(roomType.getPrice());
        roomTypeDto.setLowerBound(roomType.getLowerBound());
        roomTypeDto.setUpperBound(roomType.getUpperBound());

        return roomTypeDto;
    }

    public RoomType roomTypeCreateDtoToRoomType(RoomTypeCreateDto roomTypeCreateDto){
        RoomType roomType = new RoomType();

        roomType.setCategory(roomTypeCreateDto.getCategory());
        roomType.setPrice(roomTypeCreateDto.getPrice());
        roomType.setLowerBound(roomTypeCreateDto.getLowerBound());
        roomType.setUpperBound(roomTypeCreateDto.getUpperBound());

        return roomType;
    }

    public void updateRoomType(RoomType roomType, RoomTypeUpdateDto roomTypeUpdateDto){
        if(roomTypeUpdateDto.getCategory() != null){
            roomType.setCategory(roomTypeUpdateDto.getCategory());
        }
        if(roomTypeUpdateDto.getPrice() != 0){
            roomType.setPrice(roomTypeUpdateDto.getPrice());
        }
        if(roomTypeUpdateDto.getLowerBound() != 0){
            roomType.setLowerBound(roomTypeUpdateDto.getLowerBound());
        }
        if(roomTypeUpdateDto.getUpperBound() != 0){
            roomType.setUpperBound(roomTypeUpdateDto.getUpperBound());
        }
        //TODO DINAMICKA PROMENA SVEGA OSTALOG!
    }
}
