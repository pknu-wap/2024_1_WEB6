package com.web6.server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDTO {
    private String nickname;
    private String loginId;
    private String password;
    // 카카오 로그인에 필요한 필드 추가
    private String kakaoId;
    private String accessToken;
    private String refreshToken;
}

