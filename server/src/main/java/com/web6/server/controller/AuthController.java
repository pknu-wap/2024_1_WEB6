/*
클라이언트 로그아웃 요청을 받아 로그아웃을 처리하는 컨트롤러.
KakaoOAuth2UserUnlink를 사용하여 Kakao 계정과의 연동 해제를 수행
 */
package com.web6.server.controller;

import com.web6.server.oauth2login.user.KakaoOAuth2UserUnlink;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class AuthController {

    private final KakaoOAuth2UserUnlink kakaoOAuth2UserUnlink;

    @Autowired
    public AuthController(KakaoOAuth2UserUnlink kakaoOAuth2UserUnlink) {
        this.kakaoOAuth2UserUnlink = kakaoOAuth2UserUnlink;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = oAuth2User.getAttributes();
            String accessToken = (String) attributes.get("accessToken");

            kakaoOAuth2UserUnlink.unlink(accessToken);
            SecurityContextHolder.clearContext();
            return "redirect:/login-page";
        }

        return "redirect:/"; //oauth2 사용자가 아닌 경우
    }
}
