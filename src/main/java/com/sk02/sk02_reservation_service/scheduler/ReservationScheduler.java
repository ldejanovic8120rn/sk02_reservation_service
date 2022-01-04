package com.sk02.sk02_reservation_service.scheduler;

import com.sk02.sk02_reservation_service.domain.Reservation;
import com.sk02.sk02_reservation_service.dto.user.UserDto;
import com.sk02.sk02_reservation_service.repository.ReservationRepository;
import com.sk02.sk02_reservation_service.service.NotificationService;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@Transactional
public class ReservationScheduler {

    private final ReservationRepository reservationRepository;
    private final NotificationService notificationService;
    private final RestTemplate userServiceRestTemplate;

    public ReservationScheduler(ReservationRepository reservationRepository, NotificationService notificationService, RestTemplate userServiceRestTemplate) {
        this.reservationRepository = reservationRepository;
        this.notificationService = notificationService;
        this.userServiceRestTemplate = userServiceRestTemplate;
    }

    //@Scheduled(cron = "0 0 0/6 ? * * *")
    @Scheduled(fixedDelay = 5000, initialDelay = 5000)
    public void reminder() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, 2);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        Date startDate = c.getTime();

        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        Date endDate = c.getTime();

        List<Reservation> reservations = reservationRepository.findReservationByStartDateBetweenAndSent(startDate, endDate, false);
        for (Reservation reservation: reservations) {
            String username = reservation.getUsername();
            ResponseEntity<UserDto> userDto = userServiceRestTemplate.exchange("/users/" + username, HttpMethod.GET, null, UserDto.class);

            notificationService.remindClient(username, userDto.getBody().getEmail());
            reservation.setSent(true);
            reservationRepository.save(reservation);
        }
    }
}
