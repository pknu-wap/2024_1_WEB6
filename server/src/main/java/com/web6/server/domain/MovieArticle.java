package com.web6.server.domain;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "MovieArticle")
public class MovieArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID는 자동 생성됨
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @Column(name = "MOVIEID", nullable = false, updatable = false)
    private String movieId;

    @Column(name = "POSTER")
    private String poster;

    @Column(name = "MOVIESEQ", nullable = false, updatable = false)
    private String movieSeq;

    @Column(name = "TITLE", nullable = false, updatable = false)
    private String title;

    @Column(name = "DOCID", nullable = false, updatable = false)
    private String docId;

    @Column(name = "GRADE", nullable = false)
    private double grade = 0.0; // 평균 평점

    @Column(name = "GRADE_COUNT", nullable = false)
    private int gradeCount = 0; // 평점의 개수(==리뷰의 개수)

    @Column(name = "GRADE_SUM", nullable = false)
    private double gradeSum = 0; // 평점의 합

    @OneToMany(mappedBy = "movieArticle")
    private Set<TitleTag> titleTags;

    @OneToMany(mappedBy = "movieArticle")
    private Set<GenreTag> genreTags;

    @OneToMany(mappedBy = "movieArticle")
    private Set<ActorTag> actorTags;

    @OneToMany(mappedBy = "movieArticle")
    private Set<DirectorTag> directorTags;

    @OneToMany(mappedBy = "movieArticle")
    private Set<CountryTag> countryTags;

    @OneToMany(mappedBy = "movieArticle")
    private Set<KeyWordTag> keyWordTags;

    // Constructors, getters and setters
    public MovieArticle() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieSeq() {
        return movieSeq;
    }

    public void setMovieSeq(String movieSeq) {
        this.movieSeq = movieSeq;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() { return poster; }

    public void setPoster(String poster) { this.poster = poster; }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public double getGrade() {
        return grade;
    }

    // methods to update grade
    // 리뷰가 추가되었을 때
    public void addGrade(double grade) {
        gradeCount += 1;
        gradeSum += grade;
        updateGrade();
    }

    // 리뷰가 수정되었을 때
    public void editGrade(double oldGrade, double newGrade) {
        gradeSum -= oldGrade;
        gradeSum += newGrade;
        updateGrade();
    }

    // 리뷰가 삭제되었을 때
    public void deleteGrade(double grade) {
        gradeCount -= 1;
        gradeSum -= grade;
        updateGrade();
    }

    private void updateGrade() {
        if (gradeCount == 0) {
            grade = 0.0;
        } else {
            grade = gradeSum / (double) gradeCount;
        }
    }
}
