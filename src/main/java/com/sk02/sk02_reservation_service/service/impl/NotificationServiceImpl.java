package com.sk02.sk02_reservation_service.service.impl;

import com.sk02.sk02_reservation_service.dto.notification.NotificationTransferDto;
import com.sk02.sk02_reservation_service.dto.user.UserDto;
import com.sk02.sk02_reservation_service.helper.MessageHelper;
import com.sk02.sk02_reservation_service.service.NotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final JmsTemplate jmsTemplate;
    private final MessageHelper messageHelper;
    private final String notificationDestination;

    public NotificationServiceImpl(JmsTemplate jmsTemplate, MessageHelper messageHelper, @Value("${destination.notification}") String notificationDestination) {
        this.jmsTemplate = jmsTemplate;
        this.messageHelper = messageHelper;
        this.notificationDestination = notificationDestination;
    }

    @Override
    public void createReservationNotificationClient(String username, String email, String hotelName) {
        NotificationTransferDto notificationTransferDto = new NotificationTransferDto();

        Map<String, String> map = new HashMap<>();
        map.put("%username", username);
        map.put("%hotel", hotelName);

        notificationTransferDto.setType("CLIENT-RESERVATION");
        notificationTransferDto.setEmail(email);
        notificationTransferDto.setParameters(map);

        jmsTemplate.convertAndSend(notificationDestination, messageHelper.createTextMessage(notificationTransferDto));
    }

    @Override
    public void createReservationNotificationManager(String username, String email, String clientName) {
        NotificationTransferDto notificationTransferDto = new NotificationTransferDto();

        Map<String, String> map = new HashMap<>();
        map.put("%username", username);
        map.put("%client", clientName);

        notificationTransferDto.setType("MANAGER-RESERVATION");
        notificationTransferDto.setEmail(email);
        notificationTransferDto.setParameters(map);

        jmsTemplate.convertAndSend(notificationDestination, messageHelper.createTextMessage(notificationTransferDto));
    }

    @Override
    public void cancelReservationNotificationClient(String username, String email, String hotelName) {
        NotificationTransferDto notificationTransferDto = new NotificationTransferDto();

        Map<String, String> map = new HashMap<>();
        map.put("%username", username);
        map.put("%hotel", hotelName);

        notificationTransferDto.setType("CLIENT-CANCELLATION");
        notificationTransferDto.setEmail(email);
        notificationTransferDto.setParameters(map);

        jmsTemplate.convertAndSend(notificationDestination, messageHelper.createTextMessage(notificationTransferDto));
    }

    @Override
    public void cancelReservationNotificationManager(String username, String email, String clientName) {
        NotificationTransferDto notificationTransferDto = new NotificationTransferDto();

        Map<String, String> map = new HashMap<>();
        map.put("%username", username);
        map.put("%client", clientName);

        notificationTransferDto.setType("MANAGER-CANCELLATION");
        notificationTransferDto.setEmail(email);
        notificationTransferDto.setParameters(map);

        jmsTemplate.convertAndSend(notificationDestination, messageHelper.createTextMessage(notificationTransferDto));
    }
}
