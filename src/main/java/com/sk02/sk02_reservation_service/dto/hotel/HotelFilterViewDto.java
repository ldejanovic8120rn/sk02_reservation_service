package com.sk02.sk02_reservation_service.dto.hotel;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class HotelFilterViewDto implements Comparable{

    private String hotelId;
    private String hotelName;
    private String hotelDescription;
    private String hotelCity;

    private String roomTypeId;
    private String roomTypeCategory;
    private String roomTypePrice;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date startDate;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date endDate;

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelDescription() {
        return hotelDescription;
    }

    public void setHotelDescription(String hotelDescription) {
        this.hotelDescription = hotelDescription;
    }

    public String getHotelCity() {
        return hotelCity;
    }

    public void setHotelCity(String hotelCity) {
        this.hotelCity = hotelCity;
    }

    public String getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(String roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public String getRoomTypeCategory() {
        return roomTypeCategory;
    }

    public void setRoomTypeCategory(String roomTypeCategory) {
        this.roomTypeCategory = roomTypeCategory;
    }

    public String getRoomTypePrice() {
        return roomTypePrice;
    }

    public void setRoomTypePrice(String roomTypePrice) {
        this.roomTypePrice = roomTypePrice;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public int compareTo(Object o) {
        return this.roomTypePrice.compareTo(((HotelFilterViewDto)o).getRoomTypePrice());
    }
}
