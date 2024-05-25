package com.web6.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieRequestVo {
    String serviceKey;   //api 서비스 인증키
    int listCount;       //통합검색 출력 결과수(3 이상) (최대값 = 500)
    int startCount;      //검색 결과 시작 번호(한 페이지에 보여줄 개수) (0 이상의 10 단위 숫자)
    String collection;   //검색 대상 컬렉션 지정 (고정값 : kmdb_new)
    String query;        //검색 질의어
    String detail;       //상세정보 출력 여부 (Y or N)
    String sort;         //결과 정렬
    String releaseDts;   //기간 검색 - 개봉일 시작
    String nation;       //상세 검색 - 국가명
    String genre;        //상세 검색 - 장르명
    String type;         //상세 검색 - 유형명
    String title;        //상세 검색 - 영화명
    String director;     //상세 검색 - 감독명
    String actor;        //상세 검색 - 배우명
    String keyword;      //상세 검색 - 키워드

    String prodYear;
    String docID;        // 영화 DOCID 추가
    String movieSeq;        // 영화 movieSeq 추가
    String movieId;        // 영화 movieId 추가
    String plot;        // 영화 plot 추가

}


