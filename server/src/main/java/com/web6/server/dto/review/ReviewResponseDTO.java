package com.web6.server.dto.review;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web6.server.domain.Review;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewResponseDTO {

    //아이디를 반환하는 이유는 comment 관련 엔드포인트에 reviewId가 포함되어야 하기 때문에
    private Long id;
    //닉네임 반환은 requestDTO 에는 없음
    private String nickname;

    private String content;

    private double grade;

    private boolean spoiler;
    //아래 필드들은 닉네임 반환과 마찬가지로 requestDTO 에는 없음
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createDate;

    private boolean edit;

    private int CommentsCount;



    public ReviewResponseDTO() {}

    //리뷰를 새로 생성했을 때
    public ReviewResponseDTO(Review review, String nickname) {
        this.id = review.getId();
        this.nickname = nickname;
        this.content = review.getContent();
        this.grade = review.getGrade();
        this.spoiler = review.isSpoiler();
        this.createDate = review.getCreateDate();
        this.edit = review.isEdit();
        this.CommentsCount = review.getCommentsCount();
    }
}
