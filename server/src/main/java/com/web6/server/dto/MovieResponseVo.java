package com.web6.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieResponseVo {
    private String Query;
    private String KMAQuery;
    private int TotalCount;
    private List<MovieData> Data;

    // 생성자, getter, setter 등 생략

    // 내부 클래스로 MovieData 정의
    public static class MovieData {
        private String CollName;
        private int TotalCount;
        private int Count;
        private List<MovieResult> Result;

        // 생성자, getter, setter 등 생략

        // 내부 클래스로 MovieResult 정의
        public static class MovieResult {
            private String DOCID;
            private String movieId;
            private String movieSeq;
            private String title;
            private String titleEng;
            private String titleOrg;
            private String titleEtc;
            private String prodYear;
            private Directors directors;
            private Actors actors;
            private String nation;
            private String company;
            private Plots plots;
            private String runtime;
            private String rating;
            private String genre;
            private String kmdbUrl;

            // 생성자, getter, setter 등 생략

            // 내부 클래스로 Directors 정의
            public static class Directors {
                private List<Director> director;

                // 생성자, getter, setter 등 생략

                // 내부 클래스로 Director 정의
                public static class Director {
                    private String directorNm;
                    private String directorEnNm;
                    private String directorId;

                    // 생성자, getter, setter 등 생략
                }
            }

            // 내부 클래스로 Actors 정의
            public static class Actors {
                private List<Actor> actor;

                // 생성자, getter, setter 등 생략

                // 내부 클래스로 Actor 정의
                public static class Actor {
                    private String actorNm;
                    private String actorEnNm;
                    private String actorId;

                    // 생성자, getter, setter 등 생략
                }
            }

            // 내부 클래스로 Plots 정의
            public static class Plots {
                private List<Plot> plot;

                // 생성자, getter, setter 등 생략

                // 내부 클래스로 Plot 정의
                public static class Plot {
                    private String plotLang;
                    private String plotText;

                    // 생성자, getter, setter 등 생략
                }
            }
        }
    }
}