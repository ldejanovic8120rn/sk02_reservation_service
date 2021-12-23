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

    @ManyToOne
    private RoomType roomType;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "room")
    private List<Period> period = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public List<Period> getPeriod() {
        return period;
    }

    public void setPeriod(List<Period> period) {
        this.period = period;
    }
}
