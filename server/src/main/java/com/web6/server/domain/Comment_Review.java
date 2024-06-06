/*
Comment와 Review 테이블 간의 관계를 나타내는 조인 테이블
각 필드는 외래 키로, 각각 Comment와 Review 테이블을 참조한다.
 */
package com.web6.server.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "Comment_Review")
public class Comment_Review {
    // 복합 기본 키를 사용
    // COMMENT_ID와 REVIEW_ID 필드는 각각 Comment와 Review 엔티티의 외래 키,
    // 이 테이블은 다대다 관계의 해소를 위한 조인 테이블 역할을 수행한다.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "COMMENT_ID", nullable = false)
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "REVIEW_ID", nullable = false)
    private Review review;

    // Constructors, getters and setters
    public Comment_Review() {
    }

    public Comment_Review(Comment comment, Review review) {
        this.comment = comment;
        this.review = review;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }
}

