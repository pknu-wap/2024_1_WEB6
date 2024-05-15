package com.web6.server.controller;

import com.web6.server.dto.MovieRequestVo;
import com.web6.server.dto.MovieResponseVo;
import com.web6.server.dto.ResponseVo;
import com.web6.server.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/movie/findAll")
    public ResponseEntity<ResponseVo> testOpenAPI() {

        //requestVo 정의
        MovieRequestVo movieRequestVo = new MovieRequestVo();
        movieRequestVo.setServiceKey("E1E9179G69U2E1GV0F9B");
        movieRequestVo.setDetail("N");
        movieRequestVo.setListCount(50);


        // controller에선 request/response만 참여
        // 실제 데이터 처리 비즈니스 로직은 service로 이동
        MovieResponseVo movieResponse = movieService.getMovieInfoList(movieRequestVo);


        ResponseVo responseVo = new ResponseVo();
        log.info("Response: " + movieResponse);

        if (movieResponse == null) {
            responseVo.setUcd("99");
            responseVo.setMessage("영화 조회 실패");
        } else {
            responseVo.setUcd("00");
            responseVo.setMessage(movieResponse.toString());
        }
        return ResponseEntity.ok(responseVo);
    }
}
