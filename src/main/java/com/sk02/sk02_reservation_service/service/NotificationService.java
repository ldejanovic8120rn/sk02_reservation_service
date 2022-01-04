package com.sk02.sk02_reservation_service.service;

import com.sk02.sk02_reservation_service.dto.user.UserDto;

public interface NotificationService {

    void createReservationNotificationClient(String username, String email, String hotelName);

    void createReservationNotificationManager(String username, String email, String clientName);

    void cancelReservationNotificationClient(String username, String email, String hotelName);

    void cancelReservationNotificationManager(String username, String email, String clientName);
}
