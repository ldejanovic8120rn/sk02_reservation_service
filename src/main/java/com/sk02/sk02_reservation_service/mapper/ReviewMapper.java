package com.sk02.sk02_reservation_service.mapper;

import com.sk02.sk02_reservation_service.domain.Review;
import com.sk02.sk02_reservation_service.dto.review.ReviewCreateDto;
import com.sk02.sk02_reservation_service.dto.review.ReviewDto;
import com.sk02.sk02_reservation_service.dto.review.ReviewUpdateDto;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public Review reviewCreateDtoToReview(ReviewCreateDto reviewCreateDto){
        Review review = new Review();

        review.setComment(reviewCreateDto.getComment());
        review.setRate(reviewCreateDto.getRate());

        return review;
    }

    public ReviewDto reviewToReviewDto(Review review){
        ReviewDto reviewDto = new ReviewDto();

        reviewDto.setUsername(review.getUsername());
        reviewDto.setHotelName(review.getHotel().getName());
        reviewDto.setComment(review.getComment());
        reviewDto.setRate(review.getRate());
        reviewDto.setId(review.getId());

        return reviewDto;
    }

    public void updateReview(Review review, ReviewUpdateDto reviewUpdateDto){
        if (reviewUpdateDto.getComment() != null){
            review.setComment(reviewUpdateDto.getComment());
        }
        if (reviewUpdateDto.getRate() != 0){
            review.setRate(reviewUpdateDto.getRate());
        }
    }
}
