package com.sk02.sk02_reservation_service.service.impl;

import com.sk02.sk02_reservation_service.domain.Hotel;
import com.sk02.sk02_reservation_service.domain.Review;
import com.sk02.sk02_reservation_service.dto.review.ReviewCreateDto;
import com.sk02.sk02_reservation_service.dto.review.ReviewDto;
import com.sk02.sk02_reservation_service.dto.review.ReviewFilterDto;
import com.sk02.sk02_reservation_service.dto.review.ReviewUpdateDto;
import com.sk02.sk02_reservation_service.exception.NotFoundException;
import com.sk02.sk02_reservation_service.exception.PrivilegeException;
import com.sk02.sk02_reservation_service.mapper.ReviewMapper;
import com.sk02.sk02_reservation_service.repository.HotelRepository;
import com.sk02.sk02_reservation_service.repository.ReviewRepository;
import com.sk02.sk02_reservation_service.security.service.TokenService;
import com.sk02.sk02_reservation_service.service.ReviewService;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private static final String hotelNotFound = "Hotel with given id not found!";
    private static final String reviewNotFound = "Review with given id not found!";
    private static final String privilegeError = "You don't have permission for this action!";

    private TokenService tokenService;
    private HotelRepository hotelRepository;
    private ReviewRepository reviewRepository;
    private ReviewMapper reviewMapper;

    @Override
    public ReviewDto createReview(ReviewCreateDto reviewCreateDto, String authorization) {
        Claims claims = tokenService.parseToken(authorization.split(" ")[1]);
        String username = claims.get("username", String.class);

        Review review = reviewMapper.reviewCreateDtoToReview(reviewCreateDto);
        review.setUsername(username);
        review.setHotel(hotelRepository.findById(reviewCreateDto.getHotelId()).orElseThrow(() -> new NotFoundException(hotelNotFound)));

        reviewRepository.save(review);

        return reviewMapper.reviewToReviewDto(review);
    }

    @Override
    public ReviewDto updateReview(ReviewUpdateDto reviewUpdateDto, Long id, String authorization) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new NotFoundException(reviewNotFound));

        Claims claims = tokenService.parseToken(authorization.split(" ")[1]);
        String username = claims.get("username", String.class);

        if(!review.getUsername().equals(username)){
            throw new PrivilegeException(privilegeError);
        }
        reviewMapper.updateReview(review, reviewUpdateDto);
        reviewRepository.save(review);

        return reviewMapper.reviewToReviewDto(review);
    }

    @Override
    public void deleteReview(Long id, String authorization) {
        Claims claims = tokenService.parseToken(authorization.split(" ")[1]);
        String username = claims.get("username", String.class);

        Review review = reviewRepository.findById(id).orElseThrow(() -> new NotFoundException(reviewNotFound));

        if(!review.getUsername().equals(username)){
            throw new PrivilegeException(privilegeError);
        }

        reviewRepository.delete(review);
    }

    @Override
    public List<ReviewDto> filterReviews(ReviewFilterDto reviewFilterDto) {
        List<Hotel> hotels;
        if(reviewFilterDto.getCity() != null && reviewFilterDto.getHotelName() != null){
            hotels = hotelRepository.findHotelByCityAndName(reviewFilterDto.getCity(), reviewFilterDto.getHotelName());
        }
        else if(reviewFilterDto.getHotelName() != null){
            hotels = hotelRepository.findHotelByName(reviewFilterDto.getHotelName());
        }
        else if(reviewFilterDto.getCity() != null){
            hotels = hotelRepository.findHotelByCity(reviewFilterDto.getCity());
        }
        else {
            hotels = hotelRepository.findAll();
        }

        List<ReviewDto> reviews = new ArrayList<>();
        for (Hotel hotel: hotels){
            for (Review review: hotel.getReviews()){
                reviews.add(reviewMapper.reviewToReviewDto(review));
            }
        }

        return reviews;
    }
}
