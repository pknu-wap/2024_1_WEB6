package com.web6.server.domain;


import jakarta.persistence.*;

@Entity
@Table(name = "ReviewLike")
public class ReviewLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "REVIEW_ID", nullable = false)
    private Review review;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    //생성자
    public ReviewLike() {
    }

    //getter와 setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public Member getMember() {
        return member;
    }

    public void setUser(Member member) {
        this.member = member;
    }
}
