package com.web6.server.controller;

import com.web6.server.domain.Member;
import com.web6.server.repository.MemberRepository;
import com.web6.server.oauth2login.user.KakaoOAuth2UserUnlink;
import com.web6.server.dto.MemberDTO;
import com.web6.server.service.SignUpService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
public class SignUpController {

    private final MemberRepository memberRepository;
    private final SignUpService signUpService;

    @Autowired
    public SignUpController(SignUpService signUpService, MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
        this.signUpService = signUpService;
    }

    @Autowired
    private KakaoOAuth2UserUnlink kakaoOAuth2UserUnlink;

    @GetMapping("/")
    public String MainP(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();

        String message;
        if (!id.equals("anonymousUser")) {
            String nickname = null;

            if (authentication.getPrincipal() instanceof OAuth2User) {
                OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
                String email = (String) oAuth2User.getAttributes().get("email");
                Member member = memberRepository.findByLoginId(email);
                if (member != null) {
                    nickname = member.getNickname();
                }
            } else {
                Member member = memberRepository.findByLoginId(id);
                if (member != null) {
                    nickname = member.getNickname();
                }
            }

            if (nickname != null) {
                message = "Currently logged in as " + nickname;
            } else {
                message = "Not logged in";
            }
        } else {
            message = "Not logged in";
        }

        model.addAttribute("loginMessage", message);
        return "main";
    }

    @GetMapping("/sign-up")
    public String SignUpP() {
        return "signUp";
    }

    @PostMapping("/api/members/sign-up")
    public String SignUpProcess(@Valid MemberDTO memberDTO, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("memberDTO", memberDTO);

            Map<String, String> validatorResult = signUpService.validateHandling(errors);
            for(String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            return "signUp";
        }

        String duplicatedResult = signUpService.duplicateHandling(memberDTO);
        if(!duplicatedResult.isEmpty()) {
            model.addAttribute("memberDTO", memberDTO);
            model.addAttribute("duplicate", duplicatedResult);

            return "signUp";
        } else {
            signUpService.signUpProcess(memberDTO);
            return "redirect:/login-page";
        }
    }

    @GetMapping("/login-page")
    public String LoginP() {
        return "loginPage";
    }

    @PostMapping("/loginError")
    public String loginerror(Model model){
        model.addAttribute("errorMessage", "로그인에 실패하였습니다. 아이디와 비밀번호를 다시 입력해 주세요.");
        return "loginPage";
    }

    @GetMapping("/logout-page")
    public String logoutPage() {
        return "logoutPage"; // 일반 로그아웃 페이지의 뷰 이름 반환
    }

}
