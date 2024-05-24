package com.web6.server.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDTO {

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9]{2,10}$", message = "닉네임은 2~10자의 한글, 영문 대소문자, 숫자로 구성되어야 합니다.")
    private String nickname;

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String loginId;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자의 영문 대소문자, 숫자, 특수문자를 모두 포함해야 합니다.")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수 입력 값입니다.")
    private String confirmPassword;

    // 카카오 로그인에 필요한 필드 추가
    private String kakaoId;
    private String accessToken;
    private String refreshToken;
}

