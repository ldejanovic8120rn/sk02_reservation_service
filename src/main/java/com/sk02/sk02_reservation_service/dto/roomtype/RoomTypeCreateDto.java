package com.sk02.sk02_reservation_service.dto.roomtype;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class RoomTypeCreateDto {

    @NotNull
    private double price;

    @NotEmpty
    private String category;

    @NotNull
    private int lowerBound;

    @NotNull
    private int upperBound;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(int lowerBound) {
        this.lowerBound = lowerBound;
    }

    public int getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(int upperBound) {
        this.upperBound = upperBound;
    }
}
