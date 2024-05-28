package com.web6.server.dto;

public class MovieArticleDTO {
    private String title;
    private int gradeCount;

    public MovieArticleDTO(String title, int gradeCount) {
        this.title = title;
        this.gradeCount = gradeCount;
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
}
