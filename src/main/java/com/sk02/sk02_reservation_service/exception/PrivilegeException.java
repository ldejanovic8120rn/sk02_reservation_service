package com.sk02.sk02_reservation_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class PrivilegeException extends HttpResponseException{

    public PrivilegeException(String message) {
        super(message);
    }
}
