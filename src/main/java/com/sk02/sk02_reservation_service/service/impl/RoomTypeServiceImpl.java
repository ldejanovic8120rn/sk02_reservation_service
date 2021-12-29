package com.sk02.sk02_reservation_service.service.impl;

import com.sk02.sk02_reservation_service.domain.Hotel;
import com.sk02.sk02_reservation_service.domain.Room;
import com.sk02.sk02_reservation_service.domain.RoomType;
import com.sk02.sk02_reservation_service.dto.roomtype.RoomTypeCreateDto;
import com.sk02.sk02_reservation_service.dto.roomtype.RoomTypeDto;
import com.sk02.sk02_reservation_service.dto.roomtype.RoomTypeUpdateDto;
import com.sk02.sk02_reservation_service.exception.NotFoundException;
import com.sk02.sk02_reservation_service.mapper.RoomTypeMapper;
import com.sk02.sk02_reservation_service.repository.HotelRepository;
import com.sk02.sk02_reservation_service.repository.RoomRepository;
import com.sk02.sk02_reservation_service.repository.RoomTypeRepository;
import com.sk02.sk02_reservation_service.service.RoomTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;

@Service
@Transactional
public class RoomTypeServiceImpl implements RoomTypeService {

    private static final String hotelNotFound = "Hotel with given id not found!";
    private static final String roomTypeNotFound = "Room type with given id not found!";

    private final RoomTypeMapper roomTypeMapper;
    private final RoomTypeRepository roomTypeRepository;
    private final HotelRepository hotelRepository;
    private RoomRepository roomRepository;

    public RoomTypeServiceImpl(RoomTypeMapper roomTypeMapper, RoomTypeRepository roomTypeRepository, HotelRepository hotelRepository, RoomRepository roomRepository) {
        this.roomTypeMapper = roomTypeMapper;
        this.roomTypeRepository = roomTypeRepository;
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public RoomTypeDto createRoomType(Long hotelId, RoomTypeCreateDto roomTypeCreateDto) {
        RoomType roomType = roomTypeMapper.roomTypeCreateDtoToRoomType(roomTypeCreateDto);
        roomType.setHotel(hotelRepository.findById(hotelId).orElseThrow(() -> new NotFoundException(hotelNotFound)));

        for(int i = roomType.getLowerBound(); i < roomType.getUpperBound(); i++){
            Room room = new Room();
            room.setHotel(roomType.getHotel());
            room.setRoomNumber(i);

            room.setRoomType(roomType);
            roomType.getRooms().add(room);
        }

        return roomTypeMapper.roomTypeToRoomTypeDto(roomTypeRepository.save(roomType));
    }

    @Override
    public RoomTypeDto updateRoomType(Long id, RoomTypeUpdateDto roomTypeUpdateDto) {
        RoomType roomType = roomTypeRepository.findById(id).orElseThrow(() -> new NotFoundException(roomTypeNotFound));

        if (roomTypeUpdateDto.getLowerBound() != 0){
            updateLowerBound(roomType, roomTypeUpdateDto.getLowerBound());
        }

        if (roomTypeUpdateDto.getUpperBound() != 0) {
            updateUpperBound(roomType, roomTypeUpdateDto.getUpperBound());
        }

        roomTypeMapper.updateRoomType(roomType, roomTypeUpdateDto);
        RoomTypeDto rtd = roomTypeMapper.roomTypeToRoomTypeDto(roomTypeRepository.save(roomType));
        checkEmptyRoomTypes(roomType.getHotel().getId());

        return rtd;
    }

    @Override
    public void deleteRoomType(Long id) {
        roomTypeRepository.deleteById(id);
    }

    public void updateLowerBound(RoomType roomType, int lowerBound){
        if (lowerBound < roomType.getLowerBound()){
            for(int i = lowerBound; i < roomType.getLowerBound(); i++){
                Room room = roomRepository.findRoomByRoomNumberAndHotelId(i, roomType.getHotel().getId());
                if(room == null){
                    room = new Room();
                    room.setHotel(roomType.getHotel());
                    room.setRoomNumber(i);
                    room.setRoomType(roomType);
                    roomType.getRooms().add(room);
                }
                else{
                    room.setRoomType(roomType);
                }
            }
            roomTypeIntersected(lowerBound, roomType.getId(), roomType.getHotel().getId(), false);
        }
        else {
            for(int i = roomType.getLowerBound(); i < lowerBound; i++){
                Room room = roomRepository.findRoomByRoomNumberAndHotelId(i, roomType.getHotel().getId());
                roomRepository.delete(room);
            }
        }

        roomTypeRepository.save(roomType);
    }

    public void updateUpperBound(RoomType roomType, int upperBound){
        if(upperBound > roomType.getUpperBound()){
            for (int i = roomType.getUpperBound(); i < upperBound; i++){
                Room room = roomRepository.findRoomByRoomNumberAndHotelId(i, roomType.getHotel().getId());
                if(room == null){
                    room = new Room();
                    room.setHotel(roomType.getHotel());
                    room.setRoomNumber(i);
                    room.setRoomType(roomType);
                    roomType.getRooms().add(room);
                }
                else{
                    room.setRoomType(roomType);
                }
            }
            roomTypeIntersected(upperBound, roomType.getId(), roomType.getHotel().getId(), true);
        }
        else {
            for (int i = upperBound; i < roomType.getUpperBound(); i++){
                Room room = roomRepository.findRoomByRoomNumberAndHotelId(i, roomType.getHotel().getId());
                roomRepository.delete(room);
            }
        }

        roomTypeRepository.save(roomType);
    }

    public void roomTypeIntersected(int intersection, Long currentRoomTypeId, Long hotelId, boolean setLower){
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new NotFoundException(hotelNotFound));

        for(RoomType rt: hotel.getRoomTypes()){
            if(!(rt.getId().equals(currentRoomTypeId)) && (rt.getLowerBound() < intersection && intersection < rt.getUpperBound())){
                if(setLower){
                    rt.setLowerBound(intersection);
                }
                else {
                    rt.setUpperBound(intersection);
                }
                roomTypeRepository.save(rt);
                break;
            }
        }
    }

    public void checkEmptyRoomTypes(Long hotelId){
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new NotFoundException(hotelNotFound));

        Iterator<RoomType> rtIterator = hotel.getRoomTypes().iterator();
        while (rtIterator.hasNext()){
            RoomType rt = rtIterator.next();
            System.out.println(rt.getCategory() + " " + rt.getRooms().size());
            if(rt.getRooms().isEmpty()){
                System.out.println("usao if " + rt.getId());
                //roomTypeRepository.delete(rt);
                roomTypeRepository.deleteById(Long.valueOf("2"));
            }
        }
    }
}
