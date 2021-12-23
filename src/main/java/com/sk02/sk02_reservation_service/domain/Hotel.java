package com.sk02.sk02_reservation_service.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hotels", indexes = {@Index(columnList = "name", unique = true)})
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private int roomsNumber;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hotel")
    private List<RoomType> roomTypes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hotel")
    private List<Room> rooms = new ArrayList<>();
}
