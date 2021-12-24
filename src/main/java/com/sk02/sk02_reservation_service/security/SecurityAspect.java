package com.sk02.sk02_reservation_service.security;


import com.sk02.sk02_reservation_service.dto.hotel.HotelDto;
import com.sk02.sk02_reservation_service.dto.user.ManagerAttributesDto;
import com.sk02.sk02_reservation_service.dto.user.UserDto;
import com.sk02.sk02_reservation_service.security.service.TokenService;
import com.sk02.sk02_reservation_service.service.HotelService;
import io.jsonwebtoken.Claims;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Configuration
public class SecurityAspect {

    @Value("${oauth.jwt.secret}")
    private String jwtSecret;

    private final TokenService tokenService;
    private HotelService hotelService;
    private final RestTemplate userServiceRestTemplate;

    public SecurityAspect(TokenService tokenService, HotelService hotelService, RestTemplate userServiceRestTemplate) {
        this.tokenService = tokenService;
        this.hotelService = hotelService;
        this.userServiceRestTemplate = userServiceRestTemplate;
    }

    @Around("@annotation(com.sk02.sk02_reservation_service.security.CheckSecurity)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        //Get method signature
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        //Check for authorization parameter
        String token = null;
        for (int i = 0; i < methodSignature.getParameterNames().length; i++) {
            if (methodSignature.getParameterNames()[i].equals("authorization")) {
                //Check bearer schema
                if (joinPoint.getArgs()[i].toString().startsWith("Bearer")) {
                    //Get token
                    token = joinPoint.getArgs()[i].toString().split(" ")[1];
                }
            }
        }

        //If token is not presents return UNAUTHORIZED response
        if (token == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        //Try to parse token
        Claims claims = tokenService.parseToken(token);
        //If fails return UNAUTHORIZED response
        if (claims == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        //Check user role and proceed if user has appropriate role for specified route
        CheckSecurity checkSecurity = method.getAnnotation(CheckSecurity.class);
        String role = claims.get("role", String.class);
        if (Arrays.asList(checkSecurity.roles()).contains(role))
            return joinPoint.proceed();

        //Return FORBIDDEN if user hasn't appropriate role for specified route
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @Around("@annotation(com.sk02.sk02_reservation_service.security.CheckHotelManager)")
    public Object aroundHotelManager(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        String token = null;
        Long hotelId = null;
        for (int i = 0; i < methodSignature.getParameterNames().length; i++) {
            if (methodSignature.getParameterNames()[i].equals("authorization")) {
                if (joinPoint.getArgs()[i].toString().startsWith("Bearer")) {
                    token = joinPoint.getArgs()[i].toString().split(" ")[1];
                }
            }
            if (methodSignature.getParameterNames()[i].equals("id")){
                hotelId = (Long) joinPoint.getArgs()[i];
            }
        }

        if (token == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        Claims claims = tokenService.parseToken(token);
        if (claims == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String role = claims.get("role", String.class);
        if(role.equals("MANAGER")){
            long managerId = claims.get("id", Long.class);

            ResponseEntity<ManagerAttributesDto> managerAttributesResponseEntity = null;
            managerAttributesResponseEntity = userServiceRestTemplate.exchange("/manager-attributes/" + managerId, HttpMethod.GET, null, ManagerAttributesDto.class);

            HotelDto hotelDto = hotelService.getHotelById(hotelId);

            if(managerAttributesResponseEntity.getBody().getHotelName().equals(hotelDto.getName())){
                return joinPoint.proceed();
            }
        }
        else if(role.equals("ADMIN")){
            return joinPoint.proceed();
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

}
