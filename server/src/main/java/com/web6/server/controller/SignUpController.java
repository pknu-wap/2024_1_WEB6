package com.web6.server.controller;

import com.web6.server.domain.Member;
import com.web6.server.dto.ApiResponse;
import com.web6.server.repository.MemberRepository;
import com.web6.server.oauth2login.user.KakaoOAuth2UserUnlink;
import com.web6.server.dto.MemberDTO;
import com.web6.server.service.SignUpService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
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

    @GetMapping("/api/main")
    public ApiResponse<Map<String,String>> MainP(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();

        String sessionId = request.getSession().getId();
        Map<String, String> data = new HashMap<>();
        data.put("sessionId", sessionId);

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
                message = nickname + "님";
                return new ApiResponse<>(true, message, data);
            }
            else {
                message = "로그인을 해주세요.";
            }
        }
        else {
            message = "로그인을 해주세요.";
        }

        return new ApiResponse<>(false, message, data);
    }

    /*
    뷰를 위한 get 요청. 프론트와 연동한 후에는 필요 없을 듯함.
     */
    @GetMapping("/sign-up")
    public String SignUpP() {
        return "signUp";
    }


    @PostMapping("/api/members/sign-up")
    public ApiResponse<MemberDTO> SignUpProcess(@RequestBody @Valid MemberDTO memberDTO, Errors errors) {
        if (errors.hasErrors()) {
            Map<String, String> validatorResult = signUpService.validateHandling(errors);
            return new ApiResponse<>(false, "회원가입 실패", validatorResult, memberDTO);
        }

        String duplicateMessage = signUpService.duplicateHandling(memberDTO);
        if(!duplicateMessage.isEmpty()) {
            Map<String, String> duplicatedResult = new HashMap<>();
            duplicatedResult.put("duplicate", duplicateMessage);

            return new ApiResponse<>(false, "회원가입 실패", duplicatedResult, memberDTO);
        }
        if (!memberDTO.getPassword().equals(memberDTO.getConfirmPassword())) {
            Map<String, String> confirmPwd = new HashMap<>();
            confirmPwd.put("confirm_password", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");

            return new ApiResponse<>(false, "회원가입 실패", confirmPwd, memberDTO);
        }

        signUpService.signUpProcess(memberDTO);
        return new ApiResponse<>(true, "회원가입 성공", null);
    }

    /*
    뷰를 위한 get 요청. 프론트와 연동한 후에는 필요 없을 듯함.
     */
    @GetMapping("/login-page")
    public String LoginP() {
        return "loginPage";
    }


    /*@PostMapping("/api/loginError")
    public ApiResponse<Void> loginerror(){
        return new ApiResponse<>(false, "로그인에 실패하였습니다. 아이디와 비밀번호를 다시 입력해 주세요.", null);
    } */

    @GetMapping("/logout-page")
    public ApiResponse<Void> logoutPage() {
        return new ApiResponse<>(true, "로그아웃 페이지", null);
    }

    /*@GetMapping("/api/loginSuccess")
    public ApiResponse<Void> loginSuccess(){
        return new ApiResponse<>(true, "로그인 성공", null);
    }*/

}
