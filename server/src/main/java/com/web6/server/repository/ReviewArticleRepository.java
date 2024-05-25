package com.web6.server.repository;

import com.web6.server.domain.Member;
import com.web6.server.domain.MovieArticle;
import com.web6.server.domain.Review;
import com.web6.server.domain.Review_Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewArticleRepository extends JpaRepository<Review_Article, Long> {
    boolean existsByArticleAndReview_Writer(MovieArticle movieArticle, Member writer);
    void deleteByArticleAndReview(MovieArticle movieArticle, Review review);
}
