/*
Review와 MovieArticle 간의 관계를 설정하는 중간 테이블.
두 개의 ManyToOne 연관관계를 사용하여 Review와 MovieArticle 엔티티를 참조한다.
각 연관 관계는 JoinColumn 어노테이션을 사용하여 명시적으로 칼럼 이름을 정의한다.
Review 테이블의 REVIEW_ID는 nullable을 true로 설정함.
 */
package com.web6.server.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "Review_Article")
public class Review_Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "REVIEW_ID", nullable = true)
    private Review review;

    @ManyToOne
    @JoinColumn(name = "ARTICLE_ID", nullable = false)
    private MovieArticle article;

    // Constructors
    public Review_Article() {
    }

    public Review_Article(Review review, MovieArticle article) {
        this.review = review;
        this.article = article;
    }

    // Getters and setters
    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public MovieArticle getArticle() {
        return article;
    }

    public void setArticle(MovieArticle article) {
        this.article = article;
    }
}

