package com.web6.server.controller;

import com.web6.server.dto.ApiResponse;
import com.web6.server.dto.ReviewDTO;
import com.web6.server.service.ReviewService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@RestController
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    //Create
    @GetMapping("/movies/{movie-id}/reviews")
    public ApiResponse<Void> reviewP(@PathVariable("movie-id") Long movieId) {

        //review writer 불러오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String writerId = authentication.getName();

        //한 유저가 한 영화게시글에 쓸 수 있는 리뷰 개수는 1개로 제한
        if (reviewService.existReview(movieId, writerId)) {
            return new ApiResponse<>(false, "리뷰 작성 개수 제한을 초과하였습니다.", null); //
        }

        return new ApiResponse<>(true, "리뷰 작성 페이지로 이동합니다.", null);
    }

    @PostMapping("/api/movies/{movie-id}/reviews")
    public ApiResponse<ReviewDTO> createReview(@PathVariable("movie-id") Long movieId, @RequestBody @Valid ReviewDTO reviewDTO, Errors errors) {

        //review writer 불러오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String writerId = authentication.getName();

        //유효성 검사 탈락
        if (errors.hasErrors()) {
            return new ApiResponse<>(false, errors.getFieldError().getDefaultMessage(), reviewDTO);
        }

        //DB에 저장
        reviewService.addReview(movieId, writerId, reviewDTO);
        
        return new ApiResponse<>(true, "리뷰 작성 성공", reviewDTO);
    }

    //Read

    //Update
    @GetMapping("/movies/{movie-id}/reviewEdit/{review-id}")
    public ApiResponse<ReviewDTO> reviewEditP(@PathVariable("movie-id") Long movieId, @PathVariable("review-id") Long reviewId) {
        //이 리뷰가 현재 접속한 유저가 작성한 리뷰가 맞는 지 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String writerId = authentication.getName();

        //자기가 쓴 리뷰가 아닌 경우
        if (!reviewService.checkMyReview(reviewId, writerId)) {
            return new ApiResponse<>(false, "잘못된 요청입니다.", null);
        }

        //작성되었던 내용 불러와서 띄우기
        ReviewDTO currentReview = reviewService.getReview(reviewId);
        return new ApiResponse<>(true, "리뷰 수정 페이지로 이동합니다.", currentReview);
    }

    @PutMapping("/api/movies/{movie-id}/reviewEdit/{review-id}")
    public ApiResponse<ReviewDTO> updateReview(@PathVariable("movie-id") Long movieId, @PathVariable("review-id") Long reviewId, @RequestBody @Valid ReviewDTO editReview, Errors errors) {
        //유효성 검사
        if (errors.hasErrors()) {
            return new ApiResponse<>(false, errors.getFieldError().getDefaultMessage(), editReview);
        }

        //변경 내용 DB에 저장
        reviewService.updateReview(movieId, reviewId, editReview);
        return new ApiResponse<>(true, "리뷰 수정 성공", editReview);
    }

    // Delete
    @DeleteMapping("/api/movies/{movie-id}/reviewDelete/{review-id}")
    public ApiResponse<Void> deleteReview(@PathVariable("movie-id") Long movieId, @PathVariable("review-id") Long reviewId) {
        // 이 리뷰가 현재 접속한 유저가 작성한 리뷰가 맞는 지 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String writerId = authentication.getName();

        if (!reviewService.checkMyReview(reviewId, writerId)) {
            return new ApiResponse<>(false, "잘못된 요청입니다.", null);
        }

        reviewService.deleteReview(movieId, reviewId);
        return new ApiResponse<>(true, "리뷰 삭제 성공", null);
    }

}
