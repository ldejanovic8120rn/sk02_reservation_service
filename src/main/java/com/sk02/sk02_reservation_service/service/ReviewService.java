package com.sk02.sk02_reservation_service.service;

import com.sk02.sk02_reservation_service.dto.review.ReviewCreateDto;
import com.sk02.sk02_reservation_service.dto.review.ReviewDto;
import com.sk02.sk02_reservation_service.dto.review.ReviewFilterDto;
import com.sk02.sk02_reservation_service.dto.review.ReviewUpdateDto;

import java.util.List;

public interface ReviewService {

    ReviewDto createReview(ReviewCreateDto reviewCreateDto, String authorization);
    ReviewDto updateReview(ReviewUpdateDto reviewUpdateDto, Long id, String authorization);
    void deleteReview(Long id, String authorization);

    List<ReviewDto> filterReviews(ReviewFilterDto reviewFilterDto);
}
