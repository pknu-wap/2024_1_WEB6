package com.web6.server.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieDetailResponseVo {
    @JsonProperty("Query")
    private String query;
    @JsonProperty("KMAQuery")
    private String kmaQuery;
    @JsonProperty("TotalCount")
    private int totalCount;
    @JsonProperty("Data")
    private List<DataInfo> data;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataInfo {
        @JsonProperty("CollName")
        private String collName;
        @JsonProperty("TotalCount")
        private int totalCount;
        @JsonProperty("Count")
        private int count;
        @JsonProperty("Result")
        private List<ResultInfo> result;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ResultInfo {
            @JsonProperty("DOCID")
            private String docID;
            @JsonProperty("movieId")
            private String movieId;
            @JsonProperty("movieSeq")
            private String movieSeq;
            @JsonProperty("title")
            private String title;
            @JsonProperty("titleEng")
            private String titleEng;
            @JsonProperty("titleOrg")
            private String titleOrg;
            @JsonProperty("titleEtc")
            private String titleEtc;
            @JsonProperty("prodYear")
            private String prodYear;
            @JsonProperty("directors")
            private Directors directors;
            @JsonProperty("actors")
            private Actors actors;
            @JsonProperty("nation")
            private String nation;
            @JsonProperty("company")
            private String company;
            @JsonProperty("plots")
            private Plots plots;
            @JsonProperty("runtime")
            private String runtime;
            @JsonProperty("rating")
            private String rating;
            @JsonProperty("genre")
            private String genre;
            @JsonProperty("kmdbUrl")
            private String kmdbUrl;
            @JsonProperty("type")
            private String type;
            @JsonProperty("use")
            private String use;
            @JsonProperty("episodes")
            private String episodes;
            @JsonProperty("ratedYn")
            private String ratedYn;
            @JsonProperty("repRatDate")
            private String repRatDate;
            @JsonProperty("repRlsDate")
            private String repRlsDate;
            @JsonProperty("ratings")
            private Ratings ratings;
            @JsonProperty("keywords")
            private String keywords;
            @JsonProperty("posters")
            private String posters;
            private List<String> postersList;  // 추가된 필드
            @JsonProperty("stlls")
            private String stlls;
            private List<String> stillsList;  // 추가된 필드
            @JsonProperty("staffs")
            private Staffs staffs;
            @JsonProperty("vods")
            private Vods vods;
            @JsonProperty("openThtr")
            private String openThtr;
            @JsonProperty("stat")
            private List<Stat> stat;
            @JsonProperty("screenArea")
            private String screenArea;
            @JsonProperty("screenCnt")
            private String screenCnt;
            @JsonProperty("salesAcc")
            private String salesAcc;
            @JsonProperty("audiAcc")
            private String audiAcc;
            @JsonProperty("statSouce")
            private String statSouce;
            @JsonProperty("statDate")
            private String statDate;
            @JsonProperty("themeSong")
            private String themeSong;
            @JsonProperty("soundtrack")
            private String soundtrack;
            @JsonProperty("fLocation")
            private String fLocation;
            @JsonProperty("Awards1")
            private String awards1;
            @JsonProperty("Awards2")
            private String awards2;
            @JsonProperty("regDate")
            private String regDate;
            @JsonProperty("modDate")
            private String modDate;
            @JsonProperty("Codes")
            private Codes codes;
            @JsonProperty("CommCodes")
            private CommCodes commCodes;
            @JsonProperty("ALIAS")
            private String alias;

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Directors {
                @JsonProperty("director")
                private List<Director> director;

                @Data
                @AllArgsConstructor
                @NoArgsConstructor
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Director {
                    @JsonProperty("directorNm")
                    private String directorNm;
                    @JsonProperty("directorEnNm")
                    private String directorEnNm;
                    @JsonProperty("directorId")
                    private String directorId;
                }
            }

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Actors {
                @JsonProperty("actor")
                private List<Actor> actor;

                @Data
                @AllArgsConstructor
                @NoArgsConstructor
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Actor {
                    @JsonProperty("actorNm")
                    private String actorNm;
                    @JsonProperty("actorEnNm")
                    private String actorEnNm;
                    @JsonProperty("actorId")
                    private String actorId;
                }
            }

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Plots {
                @JsonProperty("plot")
                private List<Plot> plot;

                @Data
                @AllArgsConstructor
                @NoArgsConstructor
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Plot {
                    @JsonProperty("plotLang")
                    private String plotLang;
                    @JsonProperty("plotText")
                    private String plotText;
                }
            }

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Ratings {
                @JsonProperty("rating")
                private List<Rating> rating;

                @Data
                @AllArgsConstructor
                @NoArgsConstructor
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Rating {
                    @JsonProperty("ratingMain")
                    private String ratingMain;
                    @JsonProperty("ratingDate")
                    private String ratingDate;
                    @JsonProperty("ratingNo")
                    private String ratingNo;
                    @JsonProperty("ratingGrade")
                    private String ratingGrade;
                    @JsonProperty("releaseDate")
                    private String releaseDate;
                    @JsonProperty("runtime")
                    private String runtime;
                }
            }

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Staffs {
                @JsonProperty("staff")
                private List<Staff> staff;

                @Data
                @AllArgsConstructor
                @NoArgsConstructor
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Staff {
                    @JsonProperty("staffNm")
                    private String staffNm;
                    @JsonProperty("staffEnNm")
                    private String staffEnNm;
                    @JsonProperty("staffRoleGroup")
                    private String staffRoleGroup;
                    @JsonProperty("staffRole")
                    private String staffRole;
                    @JsonProperty("staffEtc")
                    private String staffEtc;
                    @JsonProperty("staffId")
                    private String staffId;
                }
            }

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Vods {
                @JsonProperty("vod")
                private List<Vod> vod;

                @Data
                @AllArgsConstructor
                @NoArgsConstructor
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Vod {
                    @JsonProperty("vodClass")
                    private String vodClass;
                    @JsonProperty("vodUrl")
                    private String vodUrl;
                }
            }

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Stat {
                @JsonProperty("screenArea")
                private String screenArea;
                @JsonProperty("screenCnt")
                private String screenCnt;
                @JsonProperty("salesAcc")
                private String salesAcc;
                @JsonProperty("audiAcc")
                private String audiAcc;
                @JsonProperty("statSouce")
                private String statSouce;
                @JsonProperty("statDate")
                private String statDate;
            }

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Codes {
                @JsonProperty("Code")
                private List<Code> code;

                @Data
                @AllArgsConstructor
                @NoArgsConstructor
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Code {
                    @JsonProperty("CodeNm")
                    private String codeNm;
                    @JsonProperty("CodeNo")
                    private String codeNo;
                }
            }

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class CommCodes {
                @JsonProperty("CommCode")
                private List<CommCode> commCode;

                @Data
                @AllArgsConstructor
                @NoArgsConstructor
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class CommCode {
                    @JsonProperty("CodeNm")
                    private String codeNm;
                    @JsonProperty("CodeNo")
                    private String codeNo;
                }
            }
        }
    }
}
