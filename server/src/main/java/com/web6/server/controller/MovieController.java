package com.web6.server.controller;
import com.web6.server.repository.MovieArticleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web6.server.domain.MovieArticle;
import com.web6.server.dto.MovieDetailResponseVo;
import com.web6.server.dto.MovieRequestVo;
import com.web6.server.service.MovieService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@RestController
//@Controller
@RequiredArgsConstructor
@Slf4j
public class MovieController {
    private final MovieService movieService;
    private final MovieArticleRepository movieArticleRepository;

    @GetMapping("/movie/findAll")
    public ResponseEntity<?> findAllMovies() {
        MovieRequestVo movieRequestVo = new MovieRequestVo();
        movieRequestVo.setServiceKey("MZ6960ZIAJY0W0XX7IX7");
        movieRequestVo.setDetail("Y");
        movieRequestVo.setListCount(100);

        // controller에선 request/response만 참여
        // 실제 데이터 처리 비즈니스 로직은 service로 이동
        String movieResponse = movieService.getMovieInfoList(movieRequestVo);
        ObjectMapper objectMapper = new ObjectMapper();

        MovieDetailResponseVo response = null;
        try {
            response = objectMapper.readValue(movieResponse, MovieDetailResponseVo.class);
            // 매핑된 객체를 콘솔에 출력
            System.out.println(response);
            log.info("MovieResponseVo : " + response);
        } catch (JsonMappingException e) {
            // 매핑 중에 문제가 발생한 경우
            log.error("Error occurred while mapping JSON to object: " + e.getMessage(), e);
        } catch (JsonProcessingException e) {
            // JSON 파싱 중에 문제가 발생한 경우
            log.error("Error occurred while processing JSON: " + e.getMessage(), e);
        } catch (Exception e) {
            // 기타 예외가 발생한 경우
            log.error("Unexpected error occurred: " + e.getMessage(), e);
        }

        if (response == null || response.getData().isEmpty()) {
            return ResponseEntity.status(500).body("영화 조회 실패");
        } else {
            return ResponseEntity.ok(response.getData().get(0).getResult());
        }
    }

//    ResponseVo responseVo = new ResponseVo();
//        if (response.getData().isEmpty()) {
//        responseVo.setUcd("99");
//        responseVo.setMessage("영화 조회 실패");
//    } else {
//        responseVo.setUcd("00");
//        responseVo.setMessage(response.toString());
//    }
//        return ResponseEntity.ok(responseVo);
//}

    /**
     * 영화 검색 요청을 처리하는 메소드
     *
     * @param option 검색 옵션 (title, genre, actor, director, nation)
     * @param query 검색어
     * @param model 뷰에 데이터를 전달하기 위한 모델 객체
     * @return 검색 결과 페이지 (index)
     */
    @GetMapping("/movie/search")
    public String searchMovies(@RequestParam(required = false) String option,
                                @RequestParam(required = false) String query,
                                Model model) {

        // 검색 옵션이나 검색어가 없으면 기본 검색 페이지로 돌아갑니다.
        if (option == null || query == null || option.isEmpty() || query.isEmpty()) {
            return "movieSearch";
        }

        // MovieRequestVo 객체 생성 및 기본 설정
        MovieRequestVo movieRequestVo = new MovieRequestVo();
        movieRequestVo.setServiceKey("MZ6960ZIAJY0W0XX7IX7");
        movieRequestVo.setDetail("Y");
        movieRequestVo.setListCount(100);

        // 검색 옵션에 따라 MovieRequestVo에 검색어를 설정합니다.
        switch (option) {
            case "title":
                movieRequestVo.setTitle(query);
                break;
            case "genre":
                movieRequestVo.setGenre(query);
                break;
            case "actor":
                movieRequestVo.setActor(query);
                break;
            case "director":
                movieRequestVo.setDirector(query);
                break;
            case "nation":
                movieRequestVo.setNation(query);
                break;
            default:
                // 유효하지 않은 검색 옵션인 경우 기본 검색 페이지로 돌아갑니다.
                return "movieSearch";
        }

        // MovieService를 이용해 영화 정보를 가져옵니다.
        String movieResponse = movieService.getMovieInfoList(movieRequestVo);
        ObjectMapper objectMapper = new ObjectMapper();
        MovieDetailResponseVo response = null;
        try {
            response = objectMapper.readValue(movieResponse, MovieDetailResponseVo.class);
            log.info("MovieDetailResponseVo : " + response);
        } catch (Exception e) {
            log.error("Error occurred: " + e.getMessage(), e);
        }

        // 변환된 MovieDetailResponseVo 객체가 null이 아니고 데이터가 있는 경우 모델에 추가합니다.
        if (response != null && response.getData() != null && !response.getData().isEmpty()) {
            List<MovieDetailResponseVo.DataInfo.ResultInfo> movies = response.getData().get(0).getResult();
            if (movies != null) {
                List<MovieDetailResponseVo.DataInfo.ResultInfo> filteredMovies = new ArrayList<>();
                for (MovieDetailResponseVo.DataInfo.ResultInfo movie : movies) {
                    // "에로" 장르의 영화를 필터링
                    if (!movie.getGenre().contains("에로")) {
                        // 포스터 이미지 URL을 리스트로 변환하여 할당합니다.
                        if (movie.getPosters() != null && !movie.getPosters().isEmpty()) {
                            List<String> postersList = Arrays.asList(movie.getPosters().split("\\|"));
                            movie.setPostersList(postersList);
                        }
                        // 스틸 이미지 URL을 리스트로 변환하여 할당합니다.
                        if (movie.getStlls() != null && !movie.getStlls().isEmpty()) {
                            List<String> stillsList = Arrays.asList(movie.getStlls().split("\\|"));
                            movie.setStillsList(stillsList);
                        }
                        filteredMovies.add(movie);
                    }
                }
                model.addAttribute("movies", filteredMovies);
            }
        }

        // 검색 결과 페이지를 반환.
        return "movieSearch";
    }

    // 영화 상세 페이지
    @GetMapping("/movie/detail/{movieSeq}")
    public ResponseEntity<?> findMovies(@PathVariable String movieSeq) {
        MovieRequestVo movieRequestVo = new MovieRequestVo();
        movieRequestVo.setServiceKey("MZ6960ZIAJY0W0XX7IX7");
        movieRequestVo.setDetail("Y");
        movieRequestVo.setMovieSeq(movieSeq);

        // controller에선 request/response만 참여
        // 실제 데이터 처리 비즈니스 로직은 service로 이동
        String movieResponse = movieService.getMovieDetailList(movieRequestVo);
        ObjectMapper objectMapper = new ObjectMapper();
        MovieDetailResponseVo response = null;
        try {
            response = objectMapper.readValue(movieResponse, MovieDetailResponseVo.class);
            // 매핑된 객체를 콘솔에 출력
            System.out.println(response);
            log.info("MovieResponseVo : " + response);

            // 추가된 코드: movieSeq, title, DOCID 값을 콘솔에 출력
            if (response != null && !response.getData().isEmpty()) {
                MovieDetailResponseVo.DataInfo dataInfo = response.getData().get(0);
                if (dataInfo != null && !dataInfo.getResult().isEmpty()) {
                    MovieDetailResponseVo.DataInfo.ResultInfo resultInfo = dataInfo.getResult().get(0);
                    if (resultInfo != null) {
                        String title = resultInfo.getTitle();
                        String docId = resultInfo.getDocID();
                        System.out.println("movieSeq: " + movieSeq + ", title: " + title + ", DOCID: " + docId);

                        // 중복된 movieSeq가 없는 경우에만 데이터를 저장
                        if (!movieArticleRepository.existsByMovieSeq(movieSeq)) {
                            // 영화 정보를 데이터베이스에 저장
                            MovieArticle movieArticle = new MovieArticle();
                            movieArticle.setMovieSeq(movieSeq);
                            movieArticle.setTitle(title);
                            movieArticle.setDocId(docId);
                            movieArticleRepository.save(movieArticle);
                        } else {
                            // 이미 저장된 경우에는 로그를 출력
                            log.info("Movie with movieSeq " + movieSeq + " is already saved in the database.");
                        }
                    }
                }
            }

        } catch (JsonMappingException e) {
            // 매핑 중에 문제가 발생한 경우
            log.error("Error occurred while mapping JSON to object: " + e.getMessage(), e);
        } catch (JsonProcessingException e) {
            // JSON 파싱 중에 문제가 발생한 경우
            log.error("Error occurred while processing JSON: " + e.getMessage(), e);
        } catch (Exception e) {
            // 기타 예외가 발생한 경우
            log.error("Unexpected error occurred: " + e.getMessage(), e);
        }

        if (response == null || response.getData().isEmpty()) {
            return ResponseEntity.status(500).body("영화 조회 실패");
        } else {
            return ResponseEntity.ok(response.getData().get(0).getResult());
        }
    }

    // 최신순 정렬
    @GetMapping("/movie/latest")
    public ResponseEntity<?> getLatestMovies() {
        MovieRequestVo movieRequestVo = new MovieRequestVo();
        movieRequestVo.setServiceKey("MZ6960ZIAJY0W0XX7IX7");
        movieRequestVo.setDetail("N");
        movieRequestVo.setListCount(5);
        movieRequestVo.setSort("prodYear,1"); // 최신 영화의 제작 연도를 기준으로 설정

        String movieResponse = movieService.getMovieLatestList(movieRequestVo);
        ObjectMapper objectMapper = new ObjectMapper();
        MovieDetailResponseVo response = null;
        try {
            response = objectMapper.readValue(movieResponse, MovieDetailResponseVo.class);
            log.info("Latest Movies:");
            if (response != null && !response.getData().isEmpty()) {
                List<MovieDetailResponseVo.DataInfo.ResultInfo> movies = response.getData().get(0).getResult();
                for (MovieDetailResponseVo.DataInfo.ResultInfo movie : movies) {
                    log.info("Title: " + movie.getTitle() + ", ProdYear: " + movie.getProdYear());
                }
            }
        } catch (Exception e) {
            log.error("Error occurred while fetching latest movies: " + e.getMessage(), e);
            return ResponseEntity.status(500).body("Failed to fetch latest movies");
        }

        if (response == null || response.getData().isEmpty()) {
            return ResponseEntity.status(500).body("No latest movies found");
        } else {
            return ResponseEntity.ok(response.getData().get(0).getResult());
        }
    }

}



