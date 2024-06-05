package com.web6.server.repository;

import com.web6.server.domain.Comment;
import com.web6.server.domain.Comment_Review;
import com.web6.server.domain.Member;
import com.web6.server.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentReviewRepository extends JpaRepository <Comment_Review, Long> {
    List<Comment_Review> findByReviewIdOrderByCommentCreateDateDesc(Long reviewId); //오래된 코멘트 -> 최신 코멘트 순
    void deleteByReviewAndComment(Review review, Comment comment);
    List<Comment_Review> findByCommentWriter(Member writer);
}
