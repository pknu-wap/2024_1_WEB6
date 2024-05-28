package com.web6.server.repository;

import com.web6.server.domain.MovieArticle;
import com.web6.server.dto.MovieArticleDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieArticleRepository extends JpaRepository<MovieArticle, Long> {
    boolean existsByMovieSeq(String movieSeq);
    MovieArticle findByid(Long id);
    boolean existsByid(Long id);
    // 댓글순 정렬

    @Query("SELECT new com.web6.server.dto.MovieArticleDTO(m.title, m.gradeCount) FROM MovieArticle m ORDER BY m.gradeCount DESC, m.title ASC")

//    @Query("SELECT new com.web6.server.dto.MovieArticleDTO(m.title, m.gradeCount) FROM MovieArticle m ORDER BY m.gradeCount DESC")
    List<MovieArticleDTO> findAllOrderByGradeCountDesc();
}
