package com.web6.server.dto.review;

import com.web6.server.domain.MovieArticle;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewManagementDTO {
    private String title;
    private String movieId;
    private String movieSeq;
    private double grade;

    private ReviewResponseDTO review;

    public ReviewManagementDTO() {}
    public ReviewManagementDTO(MovieArticle movieArticle, ReviewResponseDTO review) {
        this.title = movieArticle.getTitle();
        this.movieId = movieArticle.getMovieId();
        this.movieSeq = movieArticle.getMovieSeq();
        this.grade = movieArticle.getGrade();
        this.review = review;
    }
}
