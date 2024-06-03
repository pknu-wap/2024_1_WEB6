package com.web6.server.service;

import com.web6.server.dto.MovieRequestVo;
import com.web6.server.repository.MovieArticleRepository;
import com.web6.server.dto.MovieRequestVo;
import com.web6.server.repository.ReviewArticleRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final MovieArticleRepository movieArticleRepository;
    private final ReviewArticleRepository reviewArticleRepository;

    public MovieService(MovieArticleRepository movieArticleRepository, ReviewArticleRepository reviewArticleRepository) {
        this.movieArticleRepository = movieArticleRepository;
        this.reviewArticleRepository = reviewArticleRepository;
    }

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
                "&keyword={keyword}" +
                "&movieSeq={movieSeq}";

        return webClient.get()
                .uri(apiUrl, movieRequestVo.getDetail(),
                        movieRequestVo.getListCount(),
                        movieRequestVo.getServiceKey(),
                        movieRequestVo.getTitle(),
                        movieRequestVo.getGenre(),
                        movieRequestVo.getActor(),
                        movieRequestVo.getDirector(),
                        movieRequestVo.getNation(),
                        movieRequestVo.getKeyword(),
                        movieRequestVo.getMovieSeq())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    // 단일 영화 상세 정보 요청
    public String getMovieDetailList(MovieRequestVo movieRequestVo) {
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
                "&movieSeq={movieSeq}" +
                "&ServiceKey={serviceKey}";

        return webClient.get()
                .uri(apiUrl, movieRequestVo.getDetail(),
                        movieRequestVo.getMovieSeq(),
                        movieRequestVo.getServiceKey()
                )
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
    //최신순 요청
    public String getMovieLatestList(MovieRequestVo movieRequestVo) {
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
                "&sort={sort}" +
                "&ServiceKey={serviceKey}";

        return webClient.get()
                .uri(apiUrl, movieRequestVo.getDetail(),
                        movieRequestVo.getListCount(),
                        movieRequestVo.getSort(),
                        movieRequestVo.getServiceKey()
                )
                .retrieve()
                .bodyToMono(String.class)
                .block();

    }
}