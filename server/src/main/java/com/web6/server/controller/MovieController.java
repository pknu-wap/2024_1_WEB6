package com.web6.server.controller;
import com.web6.server.dto.ApiResponse;
import com.web6.server.repository.MovieArticleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web6.server.domain.MovieArticle;
import com.web6.server.dto.MovieDetailResponseVo;
import com.web6.server.dto.MovieRequestVo;
import com.web6.server.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
@RestController
//@Controller
@RequiredArgsConstructor
@Slf4j
public class MovieController {
    private final MovieService movieService;
    private final MovieArticleRepository movieArticleRepository;


    /**
     * 영화 검색 요청을 처리하는 메소드
     *
     *
     * @return 타입 : ApiResponse 객체.
     */

    @GetMapping("/movies/search/json")
    public ResponseEntity<ApiResponse<List<MovieDetailResponseVo.DataInfo.ResultInfo>>> searchMoviesJson(
            @RequestParam(required = false) String option,
            @RequestParam(required = false) String query,
            Model model) {

        // ApiResponse 객체 생성
        ApiResponse<List<MovieDetailResponseVo.DataInfo.ResultInfo>> apiResponse;

        // 검색 옵션이나 검색어가 없으면 실패 응답 반환
        if (option == null || query == null || option.isEmpty() || query.isEmpty()) {
            apiResponse = new ApiResponse<>(false, "검색 옵션과 검색어를 모두 제공해주세요.", null);
            return ResponseEntity.ok(apiResponse);
        }

        // MovieRequestVo 객체 생성 및 기본 설정
        MovieRequestVo movieRequestVo = new MovieRequestVo();
        movieRequestVo.setServiceKey("MZ6960ZIAJY0W0XX7IX7");
        movieRequestVo.setDetail("Y");
        movieRequestVo.setListCount(50);

        // 검색 옵션에 따라 MovieRequestVo에 검색어 설정
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
                // 유효하지 않은 검색 옵션인 경우 실패 응답 반환
                apiResponse = new ApiResponse<>(false, "유효하지 않은 검색 옵션입니다.", null);
                return ResponseEntity.ok(apiResponse);
        }

        // MovieService를 이용해 영화 정보 가져옴
        String movieResponse = movieService.getMovieInfoList(movieRequestVo);

        // string JSON 응답을 MovieDetailResponseVo 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        MovieDetailResponseVo response = null;
        try {
            response = objectMapper.readValue(movieResponse, MovieDetailResponseVo.class);
            log.info("MovieDetailResponseVo : " + response);
        } catch (Exception e) {
            log.error("Error occurred: " + e.getMessage(), e);
            // JSON 변환 오류 발생 시 실패 응답을 반환합니다.
            apiResponse = new ApiResponse<>(false, "영화 정보 변환 중 오류가 발생했습니다.", null);
            return ResponseEntity.ok(apiResponse);
        }

        // 변환된 MovieDetailResponseVo 객체가 null이 아니고 데이터가 있는 경우 ApiResponse에 데이터를 설정합니다.
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


                        // 제목, 장르, 배우, 감독, 국가 필드 필터링
                        movie.setTitle(cleanString(movie.getTitle()));
                        movie.setGenre(cleanString(movie.getGenre()));
                        // 배우 정보 필터링
                        if (movie.getActors() != null && movie.getActors().getActor() != null) {
                            for (MovieDetailResponseVo.DataInfo.ResultInfo.Actors.Actor actor : movie.getActors().getActor()) {
                                actor.setActorNm(cleanString(actor.getActorNm()));
                            }
                        }
                        // 감독 정보 필터링
                        if (movie.getDirectors() != null && movie.getDirectors().getDirector() != null) {
                            for (MovieDetailResponseVo.DataInfo.ResultInfo.Directors.Director director : movie.getDirectors().getDirector()) {
                                director.setDirectorNm(cleanString(director.getDirectorNm()));
                            }
                        }
                        movie.setNation(cleanString(movie.getNation()));

                        filteredMovies.add(movie);
                    }
                }
                // 성공 응답에 필터링된 영화 리스트를 설정
                apiResponse = new ApiResponse<>(true, "영화 검색 성공", filteredMovies);
                return ResponseEntity.ok(apiResponse);
            }
        }

        // 검색 결과가 없는 경우 빈 리스트 반환
        apiResponse = new ApiResponse<>(true, "검색 결과가 없습니다.", Collections.emptyList());
        return ResponseEntity.ok(apiResponse);
    }

    // 문자열 정리 함수
    private String cleanString(String str) {
        // 정규 표현식을 사용하여 문자열 정리
        str = str.replaceAll("!HS|!HE", "");
        str = str.trim();
        str = str.replaceAll("\\s+", " ");
        return str;
    }




    // 영화 상세 페이지
    @GetMapping("/movies/detail/{movieId}/{movieSeq}")
    public ResponseEntity<?> findMovies(@PathVariable String movieId, @PathVariable String movieSeq) {
        MovieRequestVo movieRequestVo = new MovieRequestVo();
        movieRequestVo.setServiceKey("MZ6960ZIAJY0W0XX7IX7");
        movieRequestVo.setDetail("Y");
        movieRequestVo.setMovieSeq(movieSeq);

        // 필요에 따라 movieId를 요청 객체에 추가합니다.
        // movieRequestVo.setMovieId(movieId);  // API 요청에 movieId가 필요하면 추가합니다.

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

            // 추가된 코드: movieId, movieSeq, title, DOCID 값을 콘솔에 출력
            if (response != null && !response.getData().isEmpty()) {
                MovieDetailResponseVo.DataInfo dataInfo = response.getData().get(0);
                if (dataInfo != null && !dataInfo.getResult().isEmpty()) {
                    List<MovieDetailResponseVo.DataInfo.ResultInfo> results = dataInfo.getResult();
                    MovieDetailResponseVo.DataInfo.ResultInfo resultInfo = null;

                    // movieId와 movieSeq가 일치하는 영화를 찾습니다.
                    for (MovieDetailResponseVo.DataInfo.ResultInfo result : results) {
                        if (movieId.equals(result.getMovieId()) && movieSeq.equals(result.getMovieSeq())) {
                            resultInfo = result;
                            break;
                        }
                    }

                    if (resultInfo != null) {
                        String title = resultInfo.getTitle();
                        String docId = resultInfo.getDocID();
                        System.out.println("movieId: " + movieId + ", movieSeq: " + movieSeq + ", title: " + title + ", DOCID: " + docId);


                        // movieId와 movieSeq의 조합으로 중복 체크
                        if (!movieArticleRepository.existsByMovieIdAndMovieSeq(movieId, movieSeq)) {
                            // 영화 정보를 데이터베이스에 저장
                            MovieArticle movieArticle = new MovieArticle();
                            movieArticle.setMovieId(movieId);
                            movieArticle.setMovieSeq(movieSeq);
                            movieArticle.setTitle(title);
                            movieArticle.setDocId(docId);

                            // 첫 번째 포스터 이미지 URL을 추출하여 저장
                            if (resultInfo.getPosters() != null && !resultInfo.getPosters().isEmpty()) {
                                String[] postersList = resultInfo.getPosters().split("\\|");
                                if (postersList.length > 0) {
                                    String firstPoster = postersList[0];
                                    movieArticle.setPoster(firstPoster);
                                    log.info("Poster URI saved in MovieArticle table: " + firstPoster);
                                }
                            }

                            movieArticleRepository.save(movieArticle);
                        } else {
                            // 이미 저장된 경우에는 로그를 출력
                            log.info("Movie with movieId " + movieId + " and movieSeq " + movieSeq + " is already saved in the database.");
                        }
                        if (!resultInfo.getGenre().contains("에로")) {
                            // 포스터 이미지 URL을 리스트로 변환하여 할당합니다.
                            if (resultInfo.getPosters() != null && !resultInfo.getPosters().isEmpty()) {
                                List<String> postersList = Arrays.asList(resultInfo.getPosters().split("\\|"));
                                resultInfo.setPostersList(postersList);
                            } else {
                                // 포스터가 없는 경우 로그를 출력합니다.
                                log.info("No posters found for movieId: " + movieId + " and movieSeq: " + movieSeq);
                            }
                          
                            // 스틸 이미지 URL을 리스트로 변환하여 할당합니다.
                            if (resultInfo.getStlls() != null && !resultInfo.getStlls().isEmpty()) {
                                List<String> stillsList = Arrays.asList(resultInfo.getStlls().split("\\|"));
                                resultInfo.setStillsList(stillsList);
                            } else {
                                // 스틸 이미지가 없는 경우 로그를 출력합니다.
                                log.info("No stills found for movieId: " + movieId + " and movieSeq: " + movieSeq);
                            }
                        }
                        // filteredMovies에 저장
                        List<MovieDetailResponseVo.DataInfo.ResultInfo> filteredMovies = new ArrayList<>();
                        filteredMovies.add(resultInfo);
                        response.getData().get(0).setResult(filteredMovies);
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
    @GetMapping("/movies/latest")
    public ResponseEntity<?> getLatestMovies() {
        MovieRequestVo movieRequestVo = new MovieRequestVo();
        movieRequestVo.setServiceKey("MZ6960ZIAJY0W0XX7IX7");
        movieRequestVo.setDetail("Y");
        movieRequestVo.setListCount(500); // 먼저 500개를 받아온 후, 필터링을 거쳐 50개만 반환.
        movieRequestVo.setSort("prodYear,1"); // 최신 영화의 제작 연도를 기준으로 설정

        String movieResponse = movieService.getMovieLatestList(movieRequestVo);
        ObjectMapper objectMapper = new ObjectMapper();
        MovieDetailResponseVo response = null;
        try {
            response = objectMapper.readValue(movieResponse, MovieDetailResponseVo.class);
            log.info("Latest Movies:");
            if (response != null && !response.getData().isEmpty()) {
                List<MovieDetailResponseVo.DataInfo.ResultInfo> movies = response.getData().get(0).getResult();
                List<MovieDetailResponseVo.DataInfo.ResultInfo> filteredMovies = new ArrayList<>();
                for (MovieDetailResponseVo.DataInfo.ResultInfo movie : movies) {
                    log.info("Title: " + movie.getTitle() + ", ProdYear: " + movie.getProdYear());

                    // "에로" 장르의 영화를 필터링하고, 제작 연도가 '2025'가 아닌 영화만 추가
                    if (!movie.getGenre().contains("에로") && !movie.getProdYear().equals("2025")) {
                        // 포스터가 빈 문자열이 아닌 경우에만 추가
                        if (movie.getPosters() != null && !movie.getPosters().isEmpty()) {
                            List<String> postersList = Arrays.asList(movie.getPosters().split("\\|"));
                            movie.setPostersList(postersList);

                            // 스틸 이미지 URL을 리스트로 변환하여 할당합니다.
                            if (movie.getStlls() != null && !movie.getStlls().isEmpty()) {
                                List<String> stillsList = Arrays.asList(movie.getStlls().split("\\|"));
                                movie.setStillsList(stillsList);
                            }

                            filteredMovies.add(movie);
                            log.info("Title: " + movie.getTitle() + ", ProdYear: " + movie.getProdYear());
                        }
                    }
                    // 50개의 영화만 가져오기
                    if (filteredMovies.size() >= 50) {
                        break;
                    }
                }
                response.getData().get(0).setResult(filteredMovies);
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



/**
 * 무비 ａｐｉ 받아오기
 * 삭제 예정
 * 쓰지 않습니다
 *
 @GetMapping("/movies/findAll")
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
 //}  */

}



