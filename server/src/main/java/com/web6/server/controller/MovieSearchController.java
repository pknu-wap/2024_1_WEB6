package com.web6.server.controller;

import com.web6.server.dto.MovieDetailResponseVo;
import com.web6.server.dto.MovieRequestVo;
import com.web6.server.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@Slf4j
public class MovieSearchController {

    private final MovieService movieService;

    @Autowired
    public MovieSearchController(MovieService movieService) {
        this.movieService = movieService;
    }

    /**
     * 영화 검색 요청을 처리하는 메소드
     *
     * @param option 검색 옵션 (title, genre, actor, director, nation)
     * @param query 검색어
     * @param model 뷰에 데이터를 전달하기 위한 모델 객체
     * @return 검색 결과 페이지 (movieSearch)
     */
    @GetMapping("/movies/search")
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
        movieRequestVo.setListCount(50);

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

        // JSON 응답을 MovieDetailResponseVo 객체로 변환합니다.
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

        // 검색 결과 페이지를 반환합니다.
        return "movieSearch";
    }
}
