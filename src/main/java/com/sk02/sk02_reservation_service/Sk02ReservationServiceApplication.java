package com.sk02.sk02_reservation_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Sk02ReservationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(Sk02ReservationServiceApplication.class, args);
    }

}
