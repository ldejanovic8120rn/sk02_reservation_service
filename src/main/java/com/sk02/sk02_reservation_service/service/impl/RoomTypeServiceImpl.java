package com.sk02.sk02_reservation_service.service.impl;

import com.sk02.sk02_reservation_service.domain.RoomType;
import com.sk02.sk02_reservation_service.dto.roomtype.RoomTypeCreateDto;
import com.sk02.sk02_reservation_service.dto.roomtype.RoomTypeDto;
import com.sk02.sk02_reservation_service.dto.roomtype.RoomTypeUpdateDto;
import com.sk02.sk02_reservation_service.exception.NotFoundException;
import com.sk02.sk02_reservation_service.mapper.RoomTypeMapper;
import com.sk02.sk02_reservation_service.repository.HotelRepository;
import com.sk02.sk02_reservation_service.repository.RoomTypeRepository;
import com.sk02.sk02_reservation_service.service.RoomTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoomTypeServiceImpl implements RoomTypeService {

    private static final String hotelNotFound = "Hotel with given id not found!";
    private static final String roomTypeNotFound = "Room type with given id not found!";

    private RoomTypeMapper roomTypeMapper;
    private RoomTypeRepository roomTypeRepository;
    private HotelRepository hotelRepository;

    @Override
    public RoomTypeDto createRoomType(Long hotelId, RoomTypeCreateDto roomTypeCreateDto) {
        RoomType roomType = roomTypeMapper.roomTypeCreateDtoToRoomType(roomTypeCreateDto);

        roomType.setHotel(hotelRepository.findById(hotelId).orElseThrow(() -> new NotFoundException(hotelNotFound)));
        //todo create rooms

        return roomTypeMapper.roomTypeToRoomTypeDto(roomTypeRepository.save(roomType));
    }

    @Override
    public RoomTypeDto updateRoomType(Long id, RoomTypeUpdateDto roomTypeUpdateDto) {
        RoomType roomType = roomTypeRepository.findById(id).orElseThrow(() -> new NotFoundException(roomTypeNotFound));
        roomTypeMapper.updateRoomType(roomType, roomTypeUpdateDto);
        return roomTypeMapper.roomTypeToRoomTypeDto(roomTypeRepository.save(roomType));
    }

    @Override
    public void deleteRoomType(Long id) {
        roomTypeRepository.deleteById(id);
    }
}
