package com.sk02.sk02_reservation_service.service.impl;

import com.sk02.sk02_reservation_service.domain.Hotel;
import com.sk02.sk02_reservation_service.domain.Period;
import com.sk02.sk02_reservation_service.domain.Room;
import com.sk02.sk02_reservation_service.domain.RoomType;
import com.sk02.sk02_reservation_service.dto.hotel.HotelFilterDto;
import com.sk02.sk02_reservation_service.dto.hotel.HotelFilterViewDto;
import com.sk02.sk02_reservation_service.exception.DateRequiredException;
import com.sk02.sk02_reservation_service.mapper.HotelMapper;
import com.sk02.sk02_reservation_service.repository.HotelRepository;
import com.sk02.sk02_reservation_service.repository.PeriodRepository;
import com.sk02.sk02_reservation_service.service.HotelFilterService;
import org.javatuples.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class HotelFilterServiceImpl implements HotelFilterService {

    private static final String dateRequiredMessage = "Start date and end date are required";

    private final HotelRepository hotelRepository;
    private final PeriodRepository periodRepository;
    private final HotelMapper hotelMapper;

    public HotelFilterServiceImpl(HotelRepository hotelRepository, PeriodRepository periodRepository, HotelMapper hotelMapper) {
        this.hotelRepository = hotelRepository;
        this.periodRepository = periodRepository;
        this.hotelMapper = hotelMapper;
    }

    @Override
    public List<HotelFilterViewDto> findHotels(HotelFilterDto hotelFilterDto) {

        if(hotelFilterDto.getStartDate() == null || hotelFilterDto.getEndDate() == null){
            throw new DateRequiredException(dateRequiredMessage);
        }

        List<Hotel> hotels;
        if(hotelFilterDto.getCity() != null && hotelFilterDto.getName() != null){
            hotels = hotelRepository.findHotelByCityAndName(hotelFilterDto.getCity(), hotelFilterDto.getName());
        }
        else if(hotelFilterDto.getName() != null){
            hotels = hotelRepository.findHotelByName(hotelFilterDto.getName());
        }
        else if(hotelFilterDto.getCity() != null){
            hotels = hotelRepository.findHotelByCity(hotelFilterDto.getCity());
        }
        else {
            hotels = hotelRepository.findAll();
        }

        return filterDates(hotels, hotelFilterDto.getStartDate(), hotelFilterDto.getEndDate(), hotelFilterDto.getPriceSort());
    }

    private List<HotelFilterViewDto> filterDates(List<Hotel> hotels, Date startDate, Date endDate, String priceSort){
        List<HotelFilterViewDto> hotelsToReturn = new ArrayList<>();
        List<Pair<String, String>> hotelCategoryPairs = new ArrayList<>();

        for (Hotel hotel: hotels){
            for (Room room: hotel.getRooms()){

                if(room.getPeriod().isEmpty()){
                    Pair<String,String> pair = Pair.with(hotel.getName(), room.getRoomType().getCategory());
                    if(!hotelCategoryPairs.contains(pair)){
                        hotelCategoryPairs.add(pair);
                        hotelsToReturn.add(hotelMapper.makeFilterView(hotel, room.getRoomType()));
                    }
                }
                else {
                    Period p1 = periodRepository.findPeriodByStartDateBeforeAndEndDateAfterAndRoomId(startDate, startDate, room.getId());
                    Period p2 = periodRepository.findPeriodByStartDateBeforeAndEndDateAfterAndRoomId(endDate, endDate, room.getId());
                    Period p3 = periodRepository.findPeriodByStartDateAfterAndEndDateBeforeAndRoomId(startDate, endDate, room.getId());

                    if(p1 == null && p2 == null && p3 == null){
                        Pair<String,String> pair = Pair.with(hotel.getName(), room.getRoomType().getCategory());
                        if(!hotelCategoryPairs.contains(pair)){
                            hotelCategoryPairs.add(pair);
                            hotelsToReturn.add(hotelMapper.makeFilterView(hotel, room.getRoomType()));
                        }
                    }
                }
            }
        }

        if(priceSort != null){
            if(priceSort.equals("ASC")){
                Collections.sort(hotelsToReturn);
            }
            else if(priceSort.equals("DESC")){
                Collections.sort(hotelsToReturn, Collections.reverseOrder());
            }
        }


        return hotelsToReturn;
    }
}
