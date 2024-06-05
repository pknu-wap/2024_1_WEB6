package com.web6.server.controller;

import com.web6.server.dto.ApiResponse;
import com.web6.server.dto.review.ReviewManagementDTO;
import com.web6.server.dto.review.ReviewRequestDTO;
import com.web6.server.dto.review.ReviewResponseDTO;
import com.web6.server.service.ReviewService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    //Create
    //리뷰 쓰기 페이지로 이동할 수 있는지
    @GetMapping("/api/movies/{movieId}/{movieSeq}/reviews")
    public ApiResponse<Void> reviewP(@PathVariable("movieId") String movieId, @PathVariable("movieSeq") String movieSeq) {

        //review writer 불러오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String writerId = authentication.getName();

        //한 유저가 한 영화게시글에 쓸 수 있는 리뷰 개수는 1개로 제한
        if (reviewService.existReview(movieId, movieSeq, writerId)) {
            return new ApiResponse<>(false, "리뷰 작성 개수 제한을 초과하였습니다.", null); //
        }

        return new ApiResponse<>(true, "리뷰 작성 페이지로 이동합니다.", null);
    }

    @PostMapping("/api/movies/{movieId}/{movieSeq}/reviews")
    public ApiResponse<ReviewRequestDTO> createReview(@PathVariable("movieId") String movieId, @PathVariable("movieSeq") String movieSeq, @RequestBody @Valid ReviewRequestDTO reviewRequestDTO, Errors errors) {

        //review writer 불러오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String writerId = authentication.getName();

        //유효성 검사 탈락
        if (errors.hasErrors()) {
            return new ApiResponse<>(false, errors.getFieldError().getDefaultMessage(), reviewRequestDTO);
        }

        //DB에 저장
        reviewService.addReview(movieId, movieSeq, writerId, reviewRequestDTO);
        
        return new ApiResponse<>(true, "리뷰 작성 성공", reviewRequestDTO);
    }

    //Read
    //리뷰를 대댓글 순으로 정렬
    @GetMapping("/api/movies/{movieId}/{movieSeq}/reviewsCommentCnt")
    public ApiResponse<List<ReviewResponseDTO>> getReviewsOrderByCommentsCount(@PathVariable("movieId") String movieId, @PathVariable("movieSeq") String movieSeq) {
        List<ReviewResponseDTO> reviewList = reviewService.getReviewsOrderByCommentCount(movieId, movieSeq);
        
        //리스트가 empty
        if(reviewList.isEmpty()) {
            return new ApiResponse<>(true, "movieSeq가 " + movieSeq + " 인 영화의 리뷰들을 최신순으로 불러왔으나, 달린 리뷰가 없음",  reviewList);
        }
        //리스트가 empty가 아닐 때
        return new ApiResponse<>(true, "리뷰들을 코멘트순으로 불러오기 성공", reviewList);
    }

    //리뷰들 최신 순으로 정렬
    @GetMapping("/api/movies/{movieId}/{movieSeq}/reviewsLatest")
    public ApiResponse<List<ReviewResponseDTO>> getReviewsOrderByLatest(@PathVariable("movieId") String movieId, @PathVariable("movieSeq") String movieSeq) {
        List<ReviewResponseDTO> reviewList = reviewService.getReviewsOrderByLatest(movieId, movieSeq);

        //리스트가 empty
        if(reviewList.isEmpty()) {
            return new ApiResponse<>(true, "movieSeq가 " + movieSeq + " 인 영화의 리뷰들을 최신순으로 불러왔으나, 달린 리뷰가 없음",  reviewList);
        }
        //리스트가 empty가 아닐 때
        return new ApiResponse<>(true, "리뷰들을 최신순으로 불러오기 성공", reviewList);
    }


    //리뷰관리
    @GetMapping("/api/writer/{nickname}/reviews")
    public ApiResponse<List<ReviewManagementDTO>> getReviewsForWriter(@PathVariable("nickname") String nickname) {
        //로그인한 유저인지 아닌지
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ApiResponse<>(false, "로그인이 필요합니다.", null);
        }
        List<ReviewManagementDTO> managementList = reviewService.getReviewsForWriter(nickname);

        //리스트가 empty
        if(managementList.isEmpty()) {
            return new ApiResponse<>(true, "닉네임이 " + nickname + "인 유저의 리뷰들을 최신순으로 불러왔으나, 작성한 리뷰가 없음",  managementList);
        }
        
        return new ApiResponse<>(true, nickname + "님의 리뷰관리 페이지로 이동", managementList);
    }


    //Update
    //리뷰 수정(쓰기) 페이지로 이동할 수 있는지
    @GetMapping("/api/reviewEdit/{review-id}")
    public ApiResponse<ReviewRequestDTO> reviewEditP(@PathVariable("review-id") Long reviewId) {
        //이 리뷰가 현재 접속한 유저가 작성한 리뷰가 맞는 지 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String writerId = authentication.getName();

        //자기가 쓴 리뷰가 아닌 경우
        if (!reviewService.checkMyReview(reviewId, writerId)) {
            return new ApiResponse<>(false, "잘못된 요청입니다.", null);
        }

        //작성되었던 내용 불러와서 띄우기
        ReviewRequestDTO currentReview = reviewService.getReview(reviewId);
        return new ApiResponse<>(true, "리뷰 수정 페이지로 이동합니다.", currentReview);
    }

    @PutMapping("/api/reviewEdit/{review-id}")
    public ApiResponse<ReviewRequestDTO> updateReview(@PathVariable("review-id") Long reviewId, @RequestBody @Valid ReviewRequestDTO editReview, Errors errors) {
        //유효성 검사
        if (errors.hasErrors()) {
            return new ApiResponse<>(false, errors.getFieldError().getDefaultMessage(), editReview);
        }

        //변경 내용 DB에 저장
        reviewService.updateReview(reviewId, editReview);
        return new ApiResponse<>(true, "리뷰 수정 성공", editReview);
    }

    // Delete
    @DeleteMapping("/api/reviewDelete/{review-id}")
    public ApiResponse<Void> deleteReview(@PathVariable("review-id") Long reviewId) {
        // 이 리뷰가 현재 접속한 유저가 작성한 리뷰가 맞는 지 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String writerId = authentication.getName();

        if (!reviewService.checkMyReview(reviewId, writerId)) {
            return new ApiResponse<>(false, "잘못된 요청입니다.", null);
        }

        reviewService.deleteReview(reviewId);
        return new ApiResponse<>(true, "리뷰 삭제 성공", null);
    }
}
