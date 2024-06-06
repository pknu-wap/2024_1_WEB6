package com.web6.server.service;

import com.web6.server.domain.*;
import com.web6.server.dto.review.ReviewManagementDTO;
import com.web6.server.dto.review.ReviewRequestDTO;
import com.web6.server.dto.review.ReviewResponseDTO;
import com.web6.server.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final MovieArticleRepository movieArticleRepository;
    private final ReviewArticleRepository reviewArticleRepository;
    private final CommentReviewRepository commentReviewRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, MemberRepository memberRepository, MovieArticleRepository movieArticleRepository, ReviewArticleRepository reviewArticleRepository, CommentReviewRepository commentReviewRepository, CommentRepository commentRepository) {
        this.reviewRepository = reviewRepository;
        this.memberRepository = memberRepository;
        this.movieArticleRepository = movieArticleRepository;
        this.reviewArticleRepository = reviewArticleRepository;
        this.commentReviewRepository = commentReviewRepository;
        this.commentRepository = commentRepository;
    }

    public boolean existReview(String movieId, String movieSeq, String writerId) {
        MovieArticle article = movieArticleRepository.findByMovieIdAndMovieSeq(movieId, movieSeq);
        Member writer = memberRepository.findByLoginId(writerId);

        boolean exist = reviewArticleRepository.existsByArticleAndReview_Writer(article, writer);
        if (exist) {return true;}
        else {return false;}
    }

   /*public boolean existArticle(Long movieId) {
        return movieArticleRepository.existsByid(movieId);
    }*/

    //리뷰 단일 불러오기
    public ReviewRequestDTO getReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID"));

        ReviewRequestDTO reviewRequestDTO = new ReviewRequestDTO();
        reviewRequestDTO.setContent(review.getContent());
        reviewRequestDTO.setGrade(review.getGrade());
        reviewRequestDTO.setSpoiler(review.isSpoiler());
        return reviewRequestDTO;
    }

    /*public String getNickname(String writerId) {
        Member writer = memberRepository.findByLoginId(writerId);
        return writer.getNickname();
    }*/

    public boolean checkMyReview(Long reviewId, String writerId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID"));

        if(review.getWriter().getLoginId().equals(writerId)){return true;}
        else return false;
    }

    @Transactional
    public void addReview(String movieId, String movieSeq, String writerId, ReviewRequestDTO reviewRequestDTO) {
        Member writer = memberRepository.findByLoginId(writerId);
        MovieArticle article = movieArticleRepository.findByMovieIdAndMovieSeq(movieId, movieSeq);

        //영화 게시글의 별점 추가
        article.addGrade(reviewRequestDTO.getGrade());

        Review review = new Review();
        review.setWriter(writer);
        review.setContent(reviewRequestDTO.getContent());
        review.setGrade(reviewRequestDTO.getGrade());
        review.setCreateDate(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        review.setEdit(false);
        review.setSpoiler(reviewRequestDTO.isSpoiler());
        review.setCommentsCount(0);

        Review saveReview = reviewRepository.save(review);

        Review_Article review_article = new Review_Article(saveReview, article);
        reviewArticleRepository.save(review_article);
    }

    public List<ReviewResponseDTO> getReviewsOrderByCommentCount(String movieId, String movieSeq) {
        //movieSeq로 movieArticle의 Id 가져오기
        Long articleId = movieArticleRepository.findByMovieIdAndMovieSeq(movieId, movieSeq).getId();
        //Id를 movieReview Repository에 넘겨서 review 리스트 가져오기
        List<Review_Article> reviewArticles = reviewArticleRepository.findByArticleIdOrderByReviewCommentsCountDesc(articleId);
        //review 리스트를 reviewResponseDTO로 변환하기
        return reviewArticles.stream()
                .map(reviewArticle -> convertToResponseDTO(reviewArticle.getReview()))
                .collect(Collectors.toList());
    }

    public List<ReviewResponseDTO> getReviewsOrderByLatest(String movieId, String movieSeq) {
        //movieSeq로 movieArticle의 Id 가져오기
        Long articleId = movieArticleRepository.findByMovieIdAndMovieSeq(movieId, movieSeq).getId();
        //Id를 movieReview Repository에 넘겨서 review 리스트 가져오기
        List<Review_Article> reviewArticles = reviewArticleRepository.findByArticleIdOrderByReviewCreateDateDesc(articleId);
        //review 리스트를 reviewResponseDTO로 변환하기
        return reviewArticles.stream()
                .map(reviewArticle -> convertToResponseDTO(reviewArticle.getReview()))
                .collect(Collectors.toList());
    }

    public List<ReviewManagementDTO> getReviewsForWriter(String nickname){
        //닉네임으로 member entitiy 반환받기
        Member writer = memberRepository.findByNickname(nickname);
        //writer가 쓴 리뷰들 찾기
        List<Review_Article> reviewArticles = reviewArticleRepository.findByReviewWriterOrderByReviewCreateDateDesc(writer);
        //managementDTO로 변환해서 반환
        return reviewArticles.stream()
                .map(this::convertToManagementDTO)
                .collect(Collectors.toList());
    }

    private ReviewResponseDTO convertToResponseDTO(Review review) {
        String nickname = review.getWriter().getNickname();
        return new ReviewResponseDTO(review, nickname);
    }

    private ReviewManagementDTO convertToManagementDTO(Review_Article review_article) {
        return new ReviewManagementDTO(review_article.getArticle(), convertToResponseDTO(review_article.getReview()));
    }

    @Transactional
    public void updateReview(Long reviewId, ReviewRequestDTO editReview) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID"));
        MovieArticle article = reviewArticleRepository.findByReview(review).getArticle();

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
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID"));
        MovieArticle article = reviewArticleRepository.findByReview(review).getArticle();

        //영화 게시글의 별점 삭제
        article.deleteGrade(review.getGrade());

        //리뷰에 달린 comments 삭제
        List<Comment_Review> commentReviews = commentReviewRepository.findByReviewIdOrderByCommentCreateDateDesc(reviewId);
        for(Comment_Review commentReview : commentReviews) {
            Comment comment = commentReview.getComment();
            commentReviewRepository.delete(commentReview); //commentReview 삭제
            commentRepository.delete(comment); //comment 삭제
        }

        reviewArticleRepository.deleteByArticleAndReview(article, review);
        reviewRepository.deleteById(reviewId);
    }


}
