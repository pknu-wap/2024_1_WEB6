package com.web6.server.service;

import com.web6.server.dto.MovieRequestVo;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class MovieService {

    public String getMovieInfoList(MovieRequestVo movieRequestVo) {
        int bufferSize = 16 * 1024 * 1024; // 16MB로 버퍼 크기 설정

        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(bufferSize))
                .build();

        WebClient webClient = WebClient.builder()
                .baseUrl("https://api.koreafilm.or.kr/openapi-data2/wisenut/search_api/search_json2.jsp")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchangeStrategies(strategies)
                .build();

        String apiUrl = "?collection=kmdb_new2" +
                "&detail={detail}" +
                "&listCount={listCount}" +
                "&ServiceKey={serviceKey}" +
                "&title={title}" +
                "&genre={genre}" +
                "&actor={actor}" +
                "&director={director}" +
                "&nation={nation}" +
                "&keyword={keyword}";

        return webClient.get()
                .uri(apiUrl, movieRequestVo.getDetail(),
                        movieRequestVo.getListCount(),
                        movieRequestVo.getServiceKey(),
                        movieRequestVo.getTitle(),
                        movieRequestVo.getGenre(),
                        movieRequestVo.getActor(),
                        movieRequestVo.getDirector(),
                        movieRequestVo.getNation(),
                        movieRequestVo.getKeyword())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}