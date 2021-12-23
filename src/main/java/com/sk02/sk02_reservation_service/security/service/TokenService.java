package com.sk02.sk02_reservation_service.security.service;

import io.jsonwebtoken.Claims;

public interface TokenService {

    String generate(Claims claims);
    Claims parseToken(String jwt);

}
