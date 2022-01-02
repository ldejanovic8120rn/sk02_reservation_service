package com.sk02.sk02_reservation_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ReservationTakenException extends HttpResponseException{

    public ReservationTakenException(String message) {
        super(message);
    }
}
