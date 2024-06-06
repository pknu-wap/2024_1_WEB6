package com.web6.server.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "Director_Tag")
public class DirectorTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "director", nullable = false, length = 20)
    private String director;

    @ManyToOne
    @JoinColumn(name = "movieArticle_Id", nullable = false)
    private MovieArticle movieArticle;

    // Constructors, getters and setters
    /** 비어있는 생성자에 대한 주석 표시
     * JPA는 엔티티에 대해 인자 없는 생성자를 요구한다.
     * 이 생성자는 애플리케이션 코드에서 직접 사용되어서는 안 된다.
     */
    public DirectorTag() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public MovieArticle getMovieArticle() {
        return movieArticle;
    }

    public void setMovieArticle(MovieArticle movieArticle) {
        this.movieArticle = movieArticle;
    }
}
