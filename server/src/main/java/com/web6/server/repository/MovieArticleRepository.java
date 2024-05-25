package com.web6.server.repository;

import com.web6.server.domain.MovieArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//public interface MovieArticleRepository extends JpaRepository<MovieArticle, Long> {
//    //MovieArticle findByid(String id);
//
//    MovieArticle findByMovieSeq(String movieSeq);
//
//}

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieArticleRepository extends JpaRepository<MovieArticle, Long> {
    // 추가적인 메소드 정의가 필요한 경우 여기에 작성
    //    //MovieArticle findByid(String id);
//
//    MovieArticle findByMovieSeq(String movieSeq);
    boolean existsByMovieSeq(String movieSeq);
}