package com.web6.server.controller;
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


@RestController
@RequiredArgsConstructor
@Slf4j
public class InitController {


    /**
     * TODO : 나중에 javadoc를 통한 주석처리 진행
     * OPEN API 테스트를 위한 예시 API
     **/
    @GetMapping("/testOpenAPI")
    public ResponseEntity<ResponseVo> testOpenAPI(@RequestParam(required = false) String data) {
        MovieRequestVo movieRequestVo = new MovieRequestVo();
        movieRequestVo.setServiceKey("MZ6960ZIAJY0W0XX7IX7");
        movieRequestVo.setDetail("N");
        movieRequestVo.setListCount(50);

        WebClient webClient = WebClient.builder()
                .baseUrl("https://api.koreafilm.or.kr/openapi-data2/wisenut/search_api")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE) // JSON 형식 요청 설정
                .build();

        String apiUrl = "/search_json2.jsp?collection=kmdb_new2&detail={detail}&listCount={listCount}&ServiceKey={serviceKey}";

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


        log.info("response : " + movieResponse.toString());

        ResponseVo responseVo = new ResponseVo();


        assert movieResponse != null;
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
