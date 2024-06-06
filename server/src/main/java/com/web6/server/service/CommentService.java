package com.web6.server.service;

import com.web6.server.domain.Comment;
import com.web6.server.domain.Comment_Review;
import com.web6.server.domain.Member;
import com.web6.server.domain.Review;
import com.web6.server.dto.comment.CommentRequestDTO;
import com.web6.server.dto.comment.CommentResponseDTO;
import com.web6.server.repository.CommentRepository;
import com.web6.server.repository.CommentReviewRepository;
import com.web6.server.repository.MemberRepository;
import com.web6.server.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private final CommentReviewRepository commentsReviewRepository ;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;

    public CommentService(CommentReviewRepository commentReviewRepository, MemberRepository memberRepository, ReviewRepository reviewRepository, CommentRepository commentRepository) {
        this.commentsReviewRepository = commentReviewRepository;
        this.memberRepository = memberRepository;
        this.reviewRepository = reviewRepository;
        this.commentRepository = commentRepository;
    }

    public CommentResponseDTO getComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID"));
        return new CommentResponseDTO(comment);
    }

    public List<CommentResponseDTO> getComments(Long reviewId) {

        List<Comment_Review> commentReviews= commentsReviewRepository.findByReviewIdOrderByCommentCreateDateDesc(reviewId); //오래된 코멘트 -> 최신 코멘트 순
        return commentReviews.stream() //스트림으로 변환해서
                .map(commentReview -> convertToDTO(commentReview.getComment())) //comment만 떼어내서 각 요소들을 dto로 변환하고
                .collect(Collectors.toList()); //이들을 다시 리스트로 묶기(정렬되어 있음)
    }

    public CommentResponseDTO convertToDTO(Comment comment) {
        String nickname = comment.getWriter().getNickname();
        return new CommentResponseDTO(comment.getId(), nickname, comment.getContent(), comment.getCreateDate(), comment.isEdit());
    }

    public boolean checkMyComment(Long commentId, String writerId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID"));

        if(comment.getWriter().getLoginId().equals(writerId)) { return true; }
        else return false;
    }

    @Transactional
    public void addComment(Long reviewId, String writerId, CommentRequestDTO requestDTO) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID"));
        Member writer = memberRepository.findByLoginId(writerId);

        //리뷰에 대댓글 개수 +1 추가
        review.addComment();

        Comment comment = new Comment();
        comment.setWriter(writer);
        comment.setContent(requestDTO.getContent());
        comment.setCreaeDate(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        comment.setEdit(false);

        Comment saveComment = commentRepository.save(comment);

        Comment_Review comment_review = new Comment_Review(saveComment, review);
        commentsReviewRepository.save(comment_review);
    }

    @Transactional
    public void updateComment(Long commentId, CommentRequestDTO requestDTO) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID"));

        comment.setContent(requestDTO.getContent());
        comment.setEdit(true);
    }

    @Transactional
    public void deleteComment(Long reviewId, Long commentId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID"));

        //리뷰에 총 대댓글 개수 -1
        review.deleteComment();

        commentsReviewRepository.deleteByReviewAndComment(review, comment);
        commentRepository.deleteById(commentId);
    }


}
