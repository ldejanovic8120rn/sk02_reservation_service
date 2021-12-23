package com.sk02.sk02_reservation_service.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rooms", uniqueConstraints = {@UniqueConstraint(columnNames = {"hotel_id", "roomNumber"})})
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int roomNumber;
    @ManyToOne
    private Hotel hotel;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "room")
    private List<Period> period = new ArrayList<>();
}
