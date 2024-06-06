package com.web6.server.dto;

public class MovieArticleDTO {
    private String title;
    private int gradeCount;
    private String poster;

    public MovieArticleDTO(String title, int gradeCount, String poster) {
        this.title = title;
        this.gradeCount = gradeCount;
        this.poster = poster;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getGradeCount() {
        return gradeCount;
    }

    public void setGradeCount(int gradeCount) {
        this.gradeCount = gradeCount;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
