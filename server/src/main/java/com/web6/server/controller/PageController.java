package com.web6.server.controller;

import com.web6.server.domain.Member;
import com.web6.server.domain.repository.MemberRepository;
import com.web6.server.dto.MemberDTO;
import com.web6.server.service.SignUpService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class PageController {

    private final MemberRepository memberRepository;
    private final SignUpService signUpService;

    @Autowired
    public PageController (SignUpService signUpService, MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
        this.signUpService = signUpService;
    }

    @GetMapping("/")
    public String mainP(Model model) {
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
    public String SignUpProcess(MemberDTO memberDTO) {

        /*
        * 중간발표 대비
        */
        //유효성 검사 결과 확인
        /*if (bindingResult.hasErrors()) {
            //유효성 검사 실패 시, 다시 회원가입 페이지로 이동
            return "redirect:/sign-up";
        }*/

        boolean isSignUpSuccessful = signUpService.signUpProcess(memberDTO);
        //회원가입 실패시, 다시 회원가입 페이지로 이동하고, 성공시에만 로그인 페이지로 이동
        if(isSignUpSuccessful) { return "redirect:/login"; }
        else { return "redirect:/sign-up"; }
    }

    @GetMapping("/login")
    public String LoginP() {
        return "login";
    }


}