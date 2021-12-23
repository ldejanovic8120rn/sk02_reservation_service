package com.sk02.sk02_reservation_service.controller;

import com.sk02.sk02_reservation_service.dto.roomtype.RoomTypeCreateDto;
import com.sk02.sk02_reservation_service.dto.roomtype.RoomTypeDto;
import com.sk02.sk02_reservation_service.dto.roomtype.RoomTypeUpdateDto;
import com.sk02.sk02_reservation_service.service.RoomTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/room-types")
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    public RoomTypeController(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

    @PostMapping("/{hotelId}")
    public ResponseEntity<RoomTypeDto> createRoomType(@PathVariable("hotelId") Long hotelId, @RequestBody RoomTypeCreateDto roomTypeCreateDto){
        return new ResponseEntity<>(roomTypeService.createRoomType(hotelId, roomTypeCreateDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomTypeDto> updateRoomType(@PathVariable("id") Long id, @RequestBody RoomTypeUpdateDto roomTypeUpdateDto){
        return new ResponseEntity<>(roomTypeService.updateRoomType(id, roomTypeUpdateDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteRoomType(@PathVariable("id") Long id){
        roomTypeService.deleteRoomType(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
