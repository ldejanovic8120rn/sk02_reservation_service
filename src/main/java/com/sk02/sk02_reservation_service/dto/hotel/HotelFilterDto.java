package com.sk02.sk02_reservation_service.dto.hotel;

import java.util.Date;

public class HotelFilterDto {

    private String name;
    private String city;

    private Date startDate;
    private Date endDate;

    private boolean priceSort;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public boolean isPriceSort() {
        return priceSort;
    }

    public void setPriceSort(boolean priceSort) {
        this.priceSort = priceSort;
    }
}
