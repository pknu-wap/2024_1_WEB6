package com.web6.server.domain;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "MovieArticle")
public class MovieArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

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

    // Add getters and setters for the tag sets
}
