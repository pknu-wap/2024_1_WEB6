package com.web6.server.controller;

import com.web6.server.dto.MemberDTO;
import com.web6.server.service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String mainP() {
        return "main";
    }

    @GetMapping("/sign-up")
    public String SignUpP() {
        return "signUp";
    }

    /*
    필드방식
    @Autowired
    private JoinService joinService;
    */

    private final SignUpService signUpService;

    @Autowired
    public PageController(SignUpService signUpService) {
        this.signUpService = signUpService;
    }
    
    @PostMapping("/api/members/sign-up")
    public String SignUpProcess(MemberDTO memberDTO) {

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