package com.web6.server.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequestDTO {

    @NotBlank(message = "대댓글 내용은 필수 입력 값입니다.")
    @Size(min = 1, max = 300, message = "대댓글 내용은 300자 이하로 입력해주세요.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9\\p{Punct} ]{1,300}$", message = "대댓글 내용은 300자 이하의 한글과 영문, 숫자와 특수 문자를 포함할 수 있습니다.")
    private String content;

}
