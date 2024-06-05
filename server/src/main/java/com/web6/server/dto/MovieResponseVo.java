package com.web6.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieResponseVo {
    @JsonProperty("Query")
    private String Query;
    @JsonProperty("KMAQuery")
    private String KMAQuery;
    @JsonProperty("TotalCount")
    private int totalCount;
    @JsonProperty
    private List<DataInfo> Data;

    // 생성자, getter, setter 등 생략

    // 내부 클래스로 MovieData 정의
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DataInfo {
        @JsonProperty("CollName")
        private String CollName;
        @JsonProperty("TotalCount")
        private int TotalCount;
        @JsonProperty("Count")
        private int Count;
        @JsonProperty
        private ArrayList<ResultInfo> Result;

        // 생성자, getter, setter 등 생략

        // 내부 클래스로 MovieResult 정의
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class ResultInfo {
            @JsonProperty("DOCID")
            private String docID;
            @JsonProperty
            private String movieId;
            @JsonProperty
            private String movieSeq;
            @JsonProperty
            private String title;
            @JsonProperty
            private String titleEng;
            @JsonProperty
            private String titleOrg;
            @JsonProperty
            private String titleEtc;
            @JsonProperty
            private String prodYear;
            @JsonProperty
            private Directors directors;
            @JsonProperty
            private Actors actors;
            @JsonProperty
            private String nation;
            @JsonProperty
            private String company;
            @JsonProperty
            private Plots plots;
            @JsonProperty
            private String runtime;
            @JsonProperty
            private String rating;
            @JsonProperty
            private String genre;
            @JsonProperty
            private String kmdbUrl;

            // 생성자, getter, setter 등 생략

            // 내부 클래스로 Directors 정의
            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            public static class Directors {
                @JsonProperty
                private List<Director> director;

                @Data
                @AllArgsConstructor
                @NoArgsConstructor
                public static class Director {
                    @JsonProperty
                    private String directorNm;
                    @JsonProperty
                    private String directorEnNm;
                    @JsonProperty
                    private String directorId;

                    // 생성자, getter, setter 등 생략
                }

                // 생성자, getter, setter 등 생략
            }

            // 내부 클래스로 Actors 정의
            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            public static class Actors {
                @JsonProperty
                private List<Actor> actor;

                // 생성자, getter, setter 등 생략

                @Data
                @AllArgsConstructor
                @NoArgsConstructor
                public static class Actor {
                    @JsonProperty
                    private String actorNm;
                    @JsonProperty
                    private String actorEnNm;
                    @JsonProperty
                    private String actorId;

                    // 생성자, getter, setter 등 생략
                }
            }

            // 내부 클래스로 Plots 정의
            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            public static class Plots {
                @JsonProperty
                private List<Plot> plot;

                // 생성자, getter, setter 등 생략

                @Data
                @AllArgsConstructor
                @NoArgsConstructor
                public static class Plot {
                    @JsonProperty
                    private String plotLang;
                    @JsonProperty
                    private String plotText;

                    // 생성자, getter, setter 등 생략
                }
            }
        }
    }
}
