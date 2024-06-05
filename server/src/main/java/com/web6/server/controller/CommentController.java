package com.web6.server.controller;

import com.web6.server.dto.ApiResponse;
import com.web6.server.dto.comment.CommentRequestDTO;
import com.web6.server.dto.comment.CommentResponseDTO;
import com.web6.server.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {

    @Autowired
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    //create
    @PostMapping("/api/reviews/{review-id}/comments")
    public ApiResponse<CommentRequestDTO> createComment(@PathVariable("review-id") long reviewId, @RequestBody @Valid CommentRequestDTO requestDTO, Errors errors) {
        //유효성 검사
        if(errors.hasErrors()) {
            return new ApiResponse<>(false, errors.getFieldError().getDefaultMessage(), requestDTO);
        }
        //writer 불러오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String writerId = authentication.getName();
        //DB에 저장
        commentService.addComment(reviewId, writerId, requestDTO);

        return new ApiResponse<>(true, "대댓글 작성 성공", requestDTO);
    }



    //read
    /*
    하나의 리뷰를 누르면, 그 리뷰의 대댓글들이 모인 페이지로 이동
    */
    @GetMapping("/api/reviews/{review-id}/comments")
    public ApiResponse<List<CommentResponseDTO>> getComments(@PathVariable("review-id") Long reviewId) {
        List<CommentResponseDTO> commentList = commentService.getComments(reviewId);

        //리스트가 empty
        if(commentList.isEmpty()) {
            return new ApiResponse<>(true, "아이디가" + reviewId + "인 리뷰에 달린 대댓글이 없음", commentList);
        }

        //리뷰에 대댓글들이 있을 때만 (나중에 리스트가 비어있을 때도 추가해야 함)
        return new ApiResponse<>(true, "리뷰의 대댓글들 불러오기 성공", commentList);
    }

    //update
    @GetMapping("/api/commentEdit/{comment-id}")
    public ApiResponse<CommentResponseDTO> CommentEditPermission(@PathVariable("comment-id") Long commentId) {
        //코멘트가 자기 것이 맞는지 검사
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String writerId = authentication.getName();

        if(!commentService.checkMyComment(commentId, writerId)) {
            return new ApiResponse<>(false, "잘못된 요청입니다.", null);
        }

        CommentResponseDTO currentComment = commentService.getComment(commentId);
        return new ApiResponse<>(true, "대댓글 수정 가능", currentComment);
    }

    @PutMapping("/api/commentEdit/{comment-id}")
    public ApiResponse<CommentRequestDTO> editComment(@PathVariable("comment-id") Long commentId, @RequestBody @Valid CommentRequestDTO editComment, Errors errors) {
        //유효성 검사
        if (errors.hasErrors()) {
            return new ApiResponse<>(false, errors.getFieldError().getDefaultMessage(), editComment);
        }

        //변경 내용 디비에 저장
        commentService.updateComment(commentId, editComment);
        return new ApiResponse<>(true, "대댓글 수정 완료", editComment);
    }

    //delete
    @DeleteMapping("/api/reviews/{review-id}/commentDelete/{comment-id}")
    public ApiResponse<Void> deleteComment(@PathVariable("review-id") Long reviewId, @PathVariable("comment-id") Long commentId) {
        //코멘트가 자기 것이 맞는지 검사
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String writerId = authentication.getName();

        if(!commentService.checkMyComment(commentId, writerId)) {
            return new ApiResponse<>(false, "잘못된 요청입니다.", null);
        }

        commentService.deleteComment(reviewId, commentId);
        return new ApiResponse<>(true, "대댓글 삭제 성공", null);
    }
}
