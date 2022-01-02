package com.sk02.sk02_reservation_service.dto.review;

import javax.validation.constraints.*;

public class ReviewCreateDto {

    @NotEmpty
    private String comment;

    @NotNull
    @Min(1) @Max(5)
    private int rate;

    @NotNull
    private Long hotelId;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }
}
