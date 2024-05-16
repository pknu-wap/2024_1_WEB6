package com.web6.server.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web6.server.dto.MovieRequestVo;
import com.web6.server.dto.MovieResponseVo;
import com.web6.server.dto.ResponseVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@Slf4j
public class InitController {


    @GetMapping("/testOpenAPI")
    public ResponseEntity<ResponseVo> testOpenAPI(@RequestParam(required = false) String data) {

        MovieRequestVo movieRequestVo = new MovieRequestVo();
        movieRequestVo.setServiceKey("MZ6960ZIAJY0W0XX7IX7");
        movieRequestVo.setDetail("N");
        movieRequestVo.setListCount(50);

        WebClient webClient = WebClient.builder()
                .baseUrl("https://api.koreafilm.or.kr/openapi-data2/wisenut/search_api/search_json2.jsp")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE) // JSON 형식 요청 설정
                .build();

        String apiUrl = "?collection=kmdb_new2&detail={detail}&listCount={listCount}&ServiceKey={serviceKey}";

        //MovieResponseVo movieResponse;
        String movieResponse = webClient.get()
                .uri(apiUrl, movieRequestVo.getDetail(),
                            movieRequestVo.getListCount(),
                            movieRequestVo.getServiceKey())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        // bodyToMono : 비동기적으로, 응답 바디를 MovieResponseVo 객체로 매핑함.
        // 이때 MovieResponseVo는 외부 API에서 받아온 JSON 응답을 자바 객채로 변환하기 위한 클래스


        log.info("json String response : " + movieResponse);



        ObjectMapper objectMapper = new ObjectMapper();


        MovieResponseVo response = null;
        try {
            response = objectMapper.readValue(movieResponse, MovieResponseVo.class);
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
}
