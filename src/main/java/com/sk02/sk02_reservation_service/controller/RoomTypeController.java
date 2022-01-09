package com.sk02.sk02_reservation_service.controller;

import com.sk02.sk02_reservation_service.dto.roomtype.RoomTypeCreateDto;
import com.sk02.sk02_reservation_service.dto.roomtype.RoomTypeDto;
import com.sk02.sk02_reservation_service.dto.roomtype.RoomTypeUpdateDto;
import com.sk02.sk02_reservation_service.security.CheckSecurity;
import com.sk02.sk02_reservation_service.service.RoomTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/room-types")
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    public RoomTypeController(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

    @GetMapping("/{hotelId}")
    @CheckSecurity(roles = {"ADMIN", "MANAGER"})
    public ResponseEntity<List<RoomTypeDto>> getRoomTypes(@RequestHeader("Authorization") String authorization, @PathVariable("hotelId") Long hotelId) {
        return new ResponseEntity<>(roomTypeService.getRoomTypes(hotelId), HttpStatus.OK);
    }

    @PostMapping("/{hotelId}")
    @CheckSecurity(roles = {"ADMIN", "MANAGER"})
    public ResponseEntity<RoomTypeDto> createRoomType(@RequestHeader("Authorization") String authorization, @PathVariable("hotelId") Long hotelId, @RequestBody RoomTypeCreateDto roomTypeCreateDto){
        return new ResponseEntity<>(roomTypeService.createRoomType(hotelId, roomTypeCreateDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @CheckSecurity(roles = {"ADMIN", "MANAGER"})
    public ResponseEntity<RoomTypeDto> updateRoomType(@RequestHeader("Authorization") String authorization, @PathVariable("id") Long id, @RequestBody RoomTypeUpdateDto roomTypeUpdateDto){
        return new ResponseEntity<>(roomTypeService.updateRoomType(id, roomTypeUpdateDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @CheckSecurity(roles = {"ADMIN", "MANAGER"})
    public ResponseEntity<HttpStatus> deleteRoomType(@RequestHeader("Authorization") String authorization, @PathVariable("id") Long id){
        roomTypeService.deleteRoomType(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
