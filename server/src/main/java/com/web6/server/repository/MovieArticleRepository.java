package com.web6.server.repository;

import com.web6.server.domain.MovieArticle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieArticleRepository extends JpaRepository<MovieArticle, Long> {
    MovieArticle findByid(Long id);
    boolean existsByid(Long id);
}
