package com.web6.server.service;

import com.web6.server.dto.MovieRequestVo;
import com.web6.server.dto.MovieResponseVo;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class MovieService {
    public MovieResponseVo getMovieInfoList(MovieRequestVo movieRequestVo) {

        WebClient webClient = WebClient.builder()
                .baseUrl("http://api.koreafilm.or.kr/openapi-data2/wisenut/search_api/search_json2.jsp")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE) // JSON 형식 요청 설정
                .build();

        String apiUrl = "?collection=kmdb_new2&detail={detail}&listCount={listCount}&ServiceKey={serviceKey}";


        MovieResponseVo movieResponse = webClient.get()
                .uri(apiUrl, movieRequestVo.getDetail(),
                        movieRequestVo.getListCount(),
                        movieRequestVo.getServiceKey())
                .retrieve()
                .bodyToMono(MovieResponseVo.class)
                .block();
        // bodyToMono : 비동기적으로, 응답 바디를 MovieResponseVo 객체로 매핑함.
        // 이때 MovieResponseVo는 외부 API에서 받아온 JSON 응답을 자바 객채로 변환하기 위한 클래스

        return movieResponse;

    }
}
