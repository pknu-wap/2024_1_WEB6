package com.web6.server.repository;

import com.web6.server.domain.Member;
import com.web6.server.domain.MovieArticle;
import com.web6.server.domain.Review;
import com.web6.server.domain.Review_Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewArticleRepository extends JpaRepository<Review_Article, Long> {
    boolean existsByArticleAndReview_Writer(MovieArticle movieArticle, Member writer);
    void deleteByArticleAndReview(MovieArticle movieArticle, Review review);
    List<Review_Article> findByArticleIdOrderByReviewCommentsCountDesc(Long articleId);
    List<Review_Article> findByArticleIdOrderByReviewCreateDateDesc(Long articleId);
    List<Review_Article> findByReviewWriterOrderByReviewCreateDateDesc(Member member);
    Review_Article findByReview (Review review);
}
