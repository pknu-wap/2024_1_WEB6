package com.web6.server.repository;

import com.web6.server.domain.MovieArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieArticleRepository extends JpaRepository<MovieArticle, Long> {
    boolean existsByMovieSeq(String movieSeq);
    MovieArticle findByid(Long id);
    MovieArticle findByMovieSeq(String movieSeq);
}
