package com.web6.server.service;

import com.web6.server.domain.Member;
import com.web6.server.domain.MovieArticle;
import com.web6.server.domain.Review;
import com.web6.server.domain.Review_Article;
import com.web6.server.dto.ReviewDTO;
import com.web6.server.repository.MemberRepository;
import com.web6.server.repository.MovieArticleRepository;
import com.web6.server.repository.ReviewArticleRepository;
import com.web6.server.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final MovieArticleRepository movieArticleRepository;
    private final ReviewArticleRepository reviewArticleRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, MemberRepository memberRepository, MovieArticleRepository movieArticleRepository, ReviewArticleRepository reviewArticleRepository ) {
        this.reviewRepository = reviewRepository;
        this.memberRepository = memberRepository;
        this.movieArticleRepository = movieArticleRepository;
        this.reviewArticleRepository = reviewArticleRepository;
    }

    public boolean existReview(Long movieId, String writerId) {
        MovieArticle article = movieArticleRepository.findByid(movieId);
        Member writer = memberRepository.findByLoginId(writerId);

        boolean exist = reviewArticleRepository.existsByArticleAndReview_Writer(article, writer);
        if (exist) {return true;}
        else {return false;}
    }

    public boolean existArticle(Long movieId) {
        return movieArticleRepository.existsByid(movieId);
    }

    public ReviewDTO getReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID"));

        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setContent(review.getContent());
        reviewDTO.setGrade(review.getGrade());
        reviewDTO.setSpoiler(review.isSpoiler());
        return reviewDTO;
    }

    public boolean checkMyReview(Long reviewId, String writerId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID"));

        if(review.getWriter().getLoginId().equals(writerId)){return true;}
        else return false;
    }

    @Transactional
    public void addReview(Long movieId, String writerId, ReviewDTO reviewDTO) {
        Member writer = memberRepository.findByLoginId(writerId);
        MovieArticle article = movieArticleRepository.findByid(movieId);

        //영화 게시글의 별점 추가
        article.addGrade(reviewDTO.getGrade());

        Review review = new Review();
        review.setWriter(writer);
        review.setContent(reviewDTO.getContent());
        review.setGrade(reviewDTO.getGrade());
        review.setCreateDate(new Date());
        review.setEdit(false);
        review.setSpoiler(reviewDTO.isSpoiler());

        Review saveReview = reviewRepository.save(review);

        Review_Article review_article = new Review_Article(saveReview, article);
        reviewArticleRepository.save(review_article);
    }

    @Transactional
    public void updateReview(Long movieId, Long reviewId, ReviewDTO editReview) {
        MovieArticle article = movieArticleRepository.findByid(movieId);
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID"));

        //영화 게시글의 별점 업데이트
        double oldGrade = review.getGrade();
        double newGrade = editReview.getGrade();
        article.editGrade(oldGrade, newGrade);

        review.setContent(editReview.getContent());
        review.setGrade(editReview.getGrade());
        review.setSpoiler(editReview.isSpoiler());
        review.setEdit(true);
    }

    @Transactional
    public void deleteReview(Long movieId, Long reviewId) {
        MovieArticle article = movieArticleRepository.findByid(movieId);
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID"));

        //영화 게시글의 별점 삭제
        article.deleteGrade(review.getGrade());
        //리뷰에 달린 comments 삭제
        //리뷰에 달린 Like 객체 삭제

        reviewArticleRepository.deleteByArticleAndReview(article, review);
        reviewRepository.deleteById(reviewId);
    }


}
