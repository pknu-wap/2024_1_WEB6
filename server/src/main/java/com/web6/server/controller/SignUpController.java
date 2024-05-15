package com.web6.server.controller;

import com.web6.server.domain.Member;
import com.web6.server.repository.MemberRepository;
import com.web6.server.dto.MemberDTO;
import com.web6.server.service.SignUpService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping("/")
    public String MainP(Model model) {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();

        /* role 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();
        */

        /*
        * 로그인 상태 표시를 위해 임시로 memberRepository에 접근해서 nickname을 받아옴
        */
        String message;
        if (!id.equals("anonymousUser")) {
            Member member = memberRepository.findByLoginId(id);
            String nickname = member.getNickname();
            message =  "Currently logged in as " + nickname;
        } else {
            message = "Not logged in";
        }

        model.addAttribute("loginMessage", message);
        //model.addAttribute("role", role);
        return "main";
    }

    @GetMapping("/sign-up")
    public String SignUpP() {
        return "signUp";
    }
    
    @PostMapping("/api/members/sign-up")
    public String SignUpProcess(@Valid MemberDTO memberDTO, Errors errors, Model model) {
        //회원가입 실패시, 다시 회원가입 페이지로 이동하고, 성공시에만 로그인 페이지로 이동

        //유효성 검사
        if (errors.hasErrors()) {
            //회원가입 실패 시 입력 데이터 값 유지
            model.addAttribute("memberDTO", memberDTO);

            //유효성 검사 통과 못한 필드와 메시지
            Map<String, String> validatorResult = signUpService.validateHandling(errors);
            for(String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            //유효성 검사 실패 시, 다시 회원가입 페이지로 이동
            return "signUp";
        }

        //중복 검사
        String duplicatedResult = signUpService.duplicateHandling(memberDTO);
        if(!duplicatedResult.isEmpty()) {
            //회원가입 실패 시 입력 데이터 값 유지
            model.addAttribute("memberDTO", memberDTO);
            model.addAttribute("duplicate", duplicatedResult);

            return "signUp";
        }
        else {
            signUpService.signUpProcess(memberDTO);
            return "redirect:/login";
        }
    }

    @GetMapping("/login")
    public String LoginP() {
        return "login";
    }

    @PostMapping("/loginError")
    public String loginerror(Model model){
        model.addAttribute("errorMessage", "로그인에 실패하였습니다. 아이디와 비밀번호를 다시 입력해 주세요.");
        return "login";
    }


}