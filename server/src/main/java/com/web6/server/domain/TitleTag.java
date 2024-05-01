package com.web6.server.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "Title_Tag")
public class TitleTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false, length = 20)
    private String title;

    @ManyToOne
    @JoinColumn(name = "movieArticle_Id", nullable = false)
    private MovieArticle movieArticle;

    // Constructors, getters and setters
    public TitleTag() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MovieArticle getMovieArticle() {
        return movieArticle;
    }

    public void setMovieArticle(MovieArticle movieArticle) {
        this.movieArticle = movieArticle;
    }
}
