package com.sk02.sk02_reservation_service.controller;

import com.sk02.sk02_reservation_service.dto.hotel.BestHotelDto;
import com.sk02.sk02_reservation_service.dto.review.ReviewCreateDto;
import com.sk02.sk02_reservation_service.dto.review.ReviewDto;
import com.sk02.sk02_reservation_service.dto.review.ReviewFilterDto;
import com.sk02.sk02_reservation_service.dto.review.ReviewUpdateDto;
import com.sk02.sk02_reservation_service.security.CheckSecurity;
import com.sk02.sk02_reservation_service.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    @CheckSecurity(roles = {"CLIENT"})
    public ResponseEntity<ReviewDto> createReview(@RequestHeader("Authorization") String authorization, @RequestBody @Valid ReviewCreateDto reviewCreateDto){
        return new ResponseEntity<>(reviewService.createReview(reviewCreateDto, authorization), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @CheckSecurity(roles = {"CLIENT"})
    public ResponseEntity<ReviewDto> updateReview(@RequestHeader("Authorization") String authorization, @PathVariable("id") Long id, @RequestBody ReviewUpdateDto reviewUpdateDto){
        return new ResponseEntity<>(reviewService.updateReview(reviewUpdateDto, id, authorization), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @CheckSecurity(roles = {"CLIENT"})
    public ResponseEntity<HttpStatus> deleteReview(@RequestHeader("Authorization") String authorization, @PathVariable("id") Long id){
        reviewService.deleteReview(id, authorization);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    @CheckSecurity(roles = {"CLIENT"})
    public ResponseEntity<List<ReviewDto>> filterReviews(@RequestHeader("Authorization") String authorization, @RequestBody ReviewFilterDto reviewFilterDto){
        return new ResponseEntity<>(reviewService.filterReviews(reviewFilterDto, PageRequest.of(0, 20)), HttpStatus.OK);
    }

    @GetMapping("/best-hotels")
    @CheckSecurity(roles = {"CLIENT"})
    public ResponseEntity<List<BestHotelDto>> bestHotels(@RequestHeader("Authorization") String authorization){
        return new ResponseEntity<>(reviewService.bestHotels(), HttpStatus.OK);
    }
}
