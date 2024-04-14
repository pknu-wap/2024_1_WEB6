package com.web6.server.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "MovieArticle_tag")
public class MovieArticle_tag {
    @Id
    @ManyToOne
    @JoinColumn(name = "ARTICLE_ID", nullable = false)
    private MovieArticle article;

    @Id
    @ManyToOne
    @JoinColumn(name = "TAG_ID", nullable = false)
    private Tag tag;

    // Constructors
    public MovieArticle_tag() {
    }

    public MovieArticle_tag(MovieArticle article, Tag tag) {
        this.article = article;
        this.tag = tag;
    }

    // Getters and Setters
    public MovieArticle getArticle() {
        return article;
    }

    public void setArticle(MovieArticle article) {
        this.article = article;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
