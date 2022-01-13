package com.sk02.sk02_reservation_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class RankException extends HttpResponseException{

    public RankException(String message) {
        super(message);
    }
}
