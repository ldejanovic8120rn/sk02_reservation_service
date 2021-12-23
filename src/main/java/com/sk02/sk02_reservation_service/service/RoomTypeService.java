package com.sk02.sk02_reservation_service.service;

import com.sk02.sk02_reservation_service.dto.roomtype.RoomTypeCreateDto;
import com.sk02.sk02_reservation_service.dto.roomtype.RoomTypeDto;
import com.sk02.sk02_reservation_service.dto.roomtype.RoomTypeUpdateDto;

public interface RoomTypeService {

    RoomTypeDto createRoomType(Long hotelId, RoomTypeCreateDto roomTypeCreateDto);

    RoomTypeDto updateRoomType(Long id, RoomTypeUpdateDto roomTypeUpdateDto);

    void deleteRoomType(Long id);
}
