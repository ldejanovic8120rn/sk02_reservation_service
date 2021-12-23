package com.sk02.sk02_reservation_service.dto.hotel;

import javax.validation.constraints.NotEmpty;

public class HotelCreateDto {

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
