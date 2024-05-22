package com.web6.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web6.server.dto.MovieDetailResponseVo;
import com.web6.server.dto.MovieRequestVo;
import com.web6.server.dto.MovieResponseVo;
import com.web6.server.dto.ResponseVo;
import com.web6.server.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MovieController {
    private final MovieService movieService;

    @GetMapping("/movie/findAll")
    public ResponseEntity<ResponseVo> testOpenAPI() {

        //requestVo 정의
        MovieRequestVo movieRequestVo = new MovieRequestVo();
        movieRequestVo.setServiceKey("MZ6960ZIAJY0W0XX7IX7");
        movieRequestVo.setDetail("Y");
        movieRequestVo.setListCount(50);

        // controller에선 request/response만 참여
        // 실제 데이터 처리 비즈니스 로직은 service로 이동
        String movieResponse = movieService.getMovieInfoList(movieRequestVo);
        //log.info("json String response : " + movieResponse);

        // ObjectMapper를 이용해서 json 문자열을 MovieResponseVo 객체로 매핑
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

        ResponseVo responseVo = new ResponseVo();
        if (response.getData().isEmpty()) {
            responseVo.setUcd("99");
            responseVo.setMessage("영화 조회 실패");
        } else {
            responseVo.setUcd("00");
            responseVo.setMessage(response.toString());
        }
        return ResponseEntity.ok(responseVo);
    }


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
            return "index";
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
                return "index";
        }

        // MovieService를 이용해 영화 정보를 가져옵니다.
        String movieResponse = movieService.getMovieInfoList(movieRequestVo);
        //log.info("json String response : " + movieResponse);

        // JSON 응답을 MovieResponseVo 객체로 변환합니다.
        ObjectMapper objectMapper = new ObjectMapper();
        MovieDetailResponseVo response = null;
        try {
            response = objectMapper.readValue(movieResponse, MovieDetailResponseVo.class);
            log.info("MovieDetailResponseVo : " + response);
        } catch (Exception e) {
            log.error("Error occurred: " + e.getMessage(), e);
        }

        // 변환된 MovieResponseVo 객체가 null이 아니고 데이터가 있는 경우 모델에 추가합니다.
        if (response != null && !response.getData().isEmpty()) {
            model.addAttribute("movies", response.getData().get(0).getResult());
        }

        // 검색 결과 페이지를 반환합니다.
        return "index";
    }
}
