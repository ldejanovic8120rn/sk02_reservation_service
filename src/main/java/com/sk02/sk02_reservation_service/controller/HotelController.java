package com.sk02.sk02_reservation_service.controller;

import com.sk02.sk02_reservation_service.dto.hotel.HotelCreateDto;
import com.sk02.sk02_reservation_service.dto.hotel.HotelDto;
import com.sk02.sk02_reservation_service.dto.hotel.HotelUpdateDto;
import com.sk02.sk02_reservation_service.security.CheckHotelManager;
import com.sk02.sk02_reservation_service.security.CheckSecurity;
import com.sk02.sk02_reservation_service.service.HotelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @PostMapping
    @CheckSecurity(roles = {"ADMIN"})
    public ResponseEntity<HotelDto> createHotel(@RequestHeader("Authorization") String authorization, @RequestBody @Valid HotelCreateDto hotelCreateDto){
        return new ResponseEntity<>(hotelService.createHotel(hotelCreateDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @CheckSecurity(roles = {"ADMIN", "MANAGER"})
    @CheckHotelManager
    public ResponseEntity<HotelDto> updateHotel(@RequestHeader("Authorization") String authorization, @PathVariable("id") Long id, @RequestBody HotelUpdateDto hotelUpdateDto){
        return new ResponseEntity<>(hotelService.updateHotel(id, hotelUpdateDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @CheckSecurity(roles = {"ADMIN"})
    public ResponseEntity<HttpStatus> deleteHotel(@RequestHeader("Authorization") String authorization, @PathVariable("id") Long id){
        hotelService.deleteHotel(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
