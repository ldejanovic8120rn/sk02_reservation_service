package com.sk02.sk02_reservation_service.controller;

import com.sk02.sk02_reservation_service.dto.hotel.*;
import com.sk02.sk02_reservation_service.security.CheckHotelManager;
import com.sk02.sk02_reservation_service.security.CheckSecurity;
import com.sk02.sk02_reservation_service.service.HotelFilterService;
import com.sk02.sk02_reservation_service.service.HotelService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    private final HotelService hotelService;
    private final HotelFilterService hotelFilterService;

    public HotelController(HotelService hotelService, HotelFilterService hotelFilterService) {
        this.hotelService = hotelService;
        this.hotelFilterService = hotelFilterService;
    }

    @PostMapping
    @CheckSecurity(roles = {"ADMIN"})
    public ResponseEntity<HotelDto> createHotel(@RequestHeader("Authorization") String authorization, @RequestBody @Valid HotelCreateDto hotelCreateDto){
        return new ResponseEntity<>(hotelService.createHotel(hotelCreateDto), HttpStatus.CREATED);
    }

    @PutMapping("/{hotel-id}")
    @CheckSecurity(roles = {"ADMIN", "MANAGER"})
    @CheckHotelManager
    public ResponseEntity<HotelDto> updateHotel(@RequestHeader("Authorization") String authorization, @PathVariable("hotel-id") Long hotelId, @RequestBody HotelUpdateDto hotelUpdateDto){
        return new ResponseEntity<>(hotelService.updateHotel(hotelId, hotelUpdateDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @CheckSecurity(roles = {"ADMIN"})
    public ResponseEntity<HttpStatus> deleteHotel(@RequestHeader("Authorization") String authorization, @PathVariable("id") Long id){
        hotelService.deleteHotel(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<HotelFilterViewDto>> filterHotels(@RequestBody HotelFilterDto hotelFilterDto){
        return new ResponseEntity<>(hotelFilterService.findHotels(hotelFilterDto, PageRequest.of(0, 20)), HttpStatus.OK);
    }
}
