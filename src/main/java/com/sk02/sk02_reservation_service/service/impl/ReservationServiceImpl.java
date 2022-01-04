package com.sk02.sk02_reservation_service.service.impl;

import com.sk02.sk02_reservation_service.domain.*;
import com.sk02.sk02_reservation_service.dto.reservation.ReservationCreateDto;
import com.sk02.sk02_reservation_service.dto.reservation.ReservationDto;
import com.sk02.sk02_reservation_service.dto.user.ManagerAttributesDto;
import com.sk02.sk02_reservation_service.dto.user.RankDto;
import com.sk02.sk02_reservation_service.dto.user.UserDto;
import com.sk02.sk02_reservation_service.exception.NotFoundException;
import com.sk02.sk02_reservation_service.exception.ReservationTakenException;
import com.sk02.sk02_reservation_service.mapper.ReservationMapper;
import com.sk02.sk02_reservation_service.repository.*;
import com.sk02.sk02_reservation_service.security.service.TokenService;
import com.sk02.sk02_reservation_service.service.NotificationService;
import com.sk02.sk02_reservation_service.service.ReservationService;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private static final String hotelNotFound = "Hotel with given id not found!";
    private static final String roomTypeNotFound = "Room type with given id not found!";
    private static final String reservationTaken = "Reservation for this time period has already been made! Be faster next time :)";
    private static final String reservationNotFound = "Reservation with given id not found!";

    private final TokenService tokenService;
    private final RestTemplate userServiceRestTemplate;
    private final ReservationRepository reservationRepository;
    private final HotelRepository hotelRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final PeriodRepository periodRepository;
    private final RoomRepository roomRepository;
    private final ReservationMapper reservationMapper;
    private final NotificationService notificationService;

    public ReservationServiceImpl(TokenService tokenService, RestTemplate userServiceRestTemplate, ReservationRepository reservationRepository, HotelRepository hotelRepository, RoomTypeRepository roomTypeRepository, PeriodRepository periodRepository, RoomRepository roomRepository, ReservationMapper reservationMapper, NotificationService notificationService) {
        this.tokenService = tokenService;
        this.userServiceRestTemplate = userServiceRestTemplate;
        this.reservationRepository = reservationRepository;
        this.hotelRepository = hotelRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.periodRepository = periodRepository;
        this.roomRepository = roomRepository;
        this.reservationMapper = reservationMapper;
        this.notificationService = notificationService;
    }

    @Override
    public List<ReservationDto> getReservationsByHotel(String authorization) {
        Claims claims = tokenService.parseToken(authorization.split(" ")[1]);
        Long id = claims.get("id", Long.class);

        ResponseEntity<ManagerAttributesDto> managerAttributesResponseEntity = null;
        managerAttributesResponseEntity = userServiceRestTemplate.exchange("/manager-attributes/" + id, HttpMethod.GET, null, ManagerAttributesDto.class);
        String hotelName = managerAttributesResponseEntity.getBody().getHotelName();

        List<Reservation> reservations = reservationRepository.findReservationByHotel_Name(hotelName);
        List<ReservationDto> reservationDtoList = new ArrayList<>();

        for(Reservation reservation: reservations){
            reservationDtoList.add(reservationMapper.reservationToReservationDto(reservation));
        }

        return reservationDtoList;
    }

    @Override
    public List<ReservationDto> getReservations(String authorization) {
        Claims claims = tokenService.parseToken(authorization.split(" ")[1]);
        String username = claims.get("username", String.class);

        List<Reservation> reservations = reservationRepository.findReservationByUsername(username);
        List<ReservationDto> reservationDtoList = new ArrayList<>();

        for(Reservation reservation: reservations){
            reservationDtoList.add(reservationMapper.reservationToReservationDto(reservation));
        }

        return reservationDtoList;
    }

    @Override
    public ReservationDto makeReservation(ReservationCreateDto reservationCreateDto, String authorization) {
        Reservation reservation = reservationMapper.reservationCreateDtoToReservation(reservationCreateDto);
        Hotel hotel = hotelRepository.findById(reservationCreateDto.getHotelId()).orElseThrow(() -> new NotFoundException(hotelNotFound));

        Claims claims = tokenService.parseToken(authorization.split(" ")[1]);
        String username = claims.get("username", String.class);
        String email = claims.get("email", String.class);
        Long id = claims.get("id", Long.class);
        reservation.setUsername(username);
        reservation.setSent(false);

        reservation.setHotel(hotel);

        RoomType roomType = roomTypeRepository.findById(reservationCreateDto.getRoomTypeId()).orElseThrow(() -> new NotFoundException(roomTypeNotFound));
        boolean roomFound = false;
        for(Room room: roomType.getRooms()){

            List<Period> p1 = periodRepository.findPeriodByStartDateBeforeAndEndDateAfterAndRoomId(reservationCreateDto.getStartDate(), reservationCreateDto.getStartDate(), room.getId());
            List<Period> p2 = periodRepository.findPeriodByStartDateBeforeAndEndDateAfterAndRoomId(reservationCreateDto.getEndDate(), reservationCreateDto.getEndDate(), room.getId());
            List<Period> p3 = periodRepository.findPeriodByStartDateAfterAndEndDateBeforeAndRoomId(reservationCreateDto.getStartDate(), reservationCreateDto.getEndDate(), room.getId());
            List<Period> p4 = periodRepository.findPeriodByStartDateAndEndDateAndRoomId(reservationCreateDto.getStartDate(), reservationCreateDto.getEndDate(), room.getId());


            if(p1.isEmpty() && p2.isEmpty() && p3.isEmpty() && p4.isEmpty()){
                Period period = new Period();
                period.setStartDate(reservationCreateDto.getStartDate());
                period.setEndDate(reservationCreateDto.getEndDate());
                period.setRoom(room);

                room.getPeriods().add(period);
                reservation.setRoom(room);
                roomFound = true;

                break;
            }
        }

        if(!roomFound){
            throw new ReservationTakenException(reservationTaken);
        }

        //TODO RETRY PATTERN
        ResponseEntity<RankDto> rankDto = null;
        rankDto = userServiceRestTemplate.exchange("/ranks/discount/" + id, HttpMethod.GET, null, RankDto.class);

        double price = getDaysBetween(reservationCreateDto.getStartDate(), reservationCreateDto.getEndDate()) * roomType.getPrice();
        if(rankDto.getBody().getDiscount() != 0){
            price *= (1 -rankDto.getBody().getDiscount());
        }
        reservation.setPrice(price);

        userServiceRestTemplate.exchange("/client-attributes/" + id, HttpMethod.PUT, null, HttpStatus.class);

        reservationRepository.save(reservation);

        //Notify client
        notificationService.createReservationNotificationClient(username, email, hotel.getName());

        //Notify manager
        ResponseEntity<UserDto> userDto = userServiceRestTemplate.exchange("/users/manager/" + hotel.getName(), HttpMethod.GET, null, UserDto.class);
        notificationService.createReservationNotificationManager(userDto.getBody().getUsername(), userDto.getBody().getEmail(), username);

        return reservationMapper.reservationToReservationDto(reservation);
    }

    @Override
    public void deleteReservationManager(Long id, String authorization) {
        Claims claims = tokenService.parseToken(authorization.split(" ")[1]);
        Long managerId = claims.get("id", Long.class);
        String managerUsername = claims.get("username", String.class);
        String email = claims.get("email", String.class);

        ResponseEntity<ManagerAttributesDto> managerAttributesResponseEntity = null;
        managerAttributesResponseEntity = userServiceRestTemplate.exchange("/manager-attributes/" + managerId, HttpMethod.GET, null, ManagerAttributesDto.class);
        String hotelName = managerAttributesResponseEntity.getBody().getHotelName();

        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new NotFoundException(reservationNotFound));
        String username = reservation.getUsername();

        if(reservation.getHotel().getName().equals(hotelName)){
            Period periodToDelete = periodRepository.findPeriodByStartDateAndEndDateAndRoomId(reservation.getStartDate(), reservation.getEndDate(), reservation.getRoom().getId()).get(0);
            reservation.getRoom().getPeriods().remove(periodToDelete);

            periodRepository.delete(periodToDelete);
            roomRepository.save(reservation.getRoom());
            reservationRepository.delete(reservation);

            //Notify client
            ResponseEntity<UserDto> userDto = userServiceRestTemplate.exchange("/users/" + username, HttpMethod.GET, null, UserDto.class);
            notificationService.cancelReservationNotificationClient(username, userDto.getBody().getEmail(), reservation.getHotel().getName());

            //Notify manager
            notificationService.cancelReservationNotificationManager(managerUsername, email, username);

            userServiceRestTemplate.exchange("/client-attributes/cancel/" + username, HttpMethod.PUT, null, HttpStatus.class);
        }

    }

    @Override
    public void deleteReservation(Long id, String authorization) {
        Claims claims = tokenService.parseToken(authorization.split(" ")[1]);
        String username = claims.get("username", String.class);
        String email = claims.get("email", String.class);

        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new NotFoundException(reservationNotFound));

        if(username.equals(reservation.getUsername())){
            Period periodToDelete = periodRepository.findPeriodByStartDateAndEndDateAndRoomId(reservation.getStartDate(), reservation.getEndDate(), reservation.getRoom().getId()).get(0);
            reservation.getRoom().getPeriods().remove(periodToDelete);

            periodRepository.delete(periodToDelete);
            roomRepository.save(reservation.getRoom());
            reservationRepository.delete(reservation);

            //Notify client
            notificationService.cancelReservationNotificationClient(username, email, reservation.getHotel().getName());

            //Notify manager
            ResponseEntity<UserDto> userDto = userServiceRestTemplate.exchange("/users/manager/" + reservation.getHotel().getName(), HttpMethod.GET, null, UserDto.class);
            notificationService.cancelReservationNotificationManager(userDto.getBody().getUsername(), userDto.getBody().getEmail(), username);

            userServiceRestTemplate.exchange("/client-attributes/cancel/" + username, HttpMethod.PUT, null, HttpStatus.class);
        }
    }

    private int getDaysBetween(Date startDate, Date endDate){
        long daysDifference = endDate.getTime() - startDate.getTime();
        return (int)TimeUnit.DAYS.convert(daysDifference, TimeUnit.MILLISECONDS);
    }
}
