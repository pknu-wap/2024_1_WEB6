/*
클라이언트 로그아웃 요청을 받아 로그아웃을 처리하는 컨트롤러.
KakaoOAuth2UserUnlink를 사용하여 Kakao 계정과의 연동 해제를 수행
 */
package com.web6.server.controller;

import com.web6.server.domain.Member;
import com.web6.server.repository.MemberRepository;
import com.web6.server.oauth2login.user.KakaoOAuth2UserUnlink;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class AuthController {

    private final KakaoOAuth2UserUnlink kakaoOAuth2UserUnlink;
    private final MemberRepository memberRepository;

    @Autowired
    public AuthController(KakaoOAuth2UserUnlink kakaoOAuth2UserUnlink, MemberRepository memberRepository) {
        this.kakaoOAuth2UserUnlink = kakaoOAuth2UserUnlink;
        this.memberRepository = memberRepository;
    }

    @GetMapping("/test")
    @ResponseBody
    public String test(){
        return "logined";
    }

    /*@GetMapping("/api/logout")
    public String logout(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = oAuth2User.getAttributes();
            String accessToken = (String) attributes.get("accessToken");

            // Kakao 연동 해제
            kakaoOAuth2UserUnlink.unlink(accessToken);

            // 사용자의 accessToken과 refreshToken 삭제
            String kakaoId = (String) attributes.get("id");
            Member member = memberRepository.findByKakaoId(kakaoId);
            if (member != null) {
                member.setAccessToken(null);
                member.setRefreshToken(null);
                memberRepository.save(member);
            }

            SecurityContextHolder.clearContext();
            return "redirect:/main_page";
        }

        return "redirect:/index"; //oauth2 사용자가 아닌 경우
    }*/
}