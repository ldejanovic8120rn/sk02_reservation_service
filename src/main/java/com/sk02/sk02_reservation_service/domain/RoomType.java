package com.sk02.sk02_reservation_service.domain;

import javax.persistence.*;

@Entity
@Table(name = "room_types")
public class RoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double price;
    private String category;

    private int lowerBound;
    private int upperBound;

    @ManyToOne
    private Hotel hotel;
}
