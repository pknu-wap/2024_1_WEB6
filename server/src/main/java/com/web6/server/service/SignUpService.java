package com.web6.server.service;

import com.web6.server.domain.Member;
import com.web6.server.domain.repository.MemberRepository;
import com.web6.server.dto.MemberDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.View;

import java.util.HashMap;
import java.util.Map;

@Service
public class SignUpService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final View error;

    @Autowired
    public SignUpService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder, View error) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.error = error;
    }

    /*
    리턴타입은 회원가입 성공시 true, 실패시 false를 반환
    */
    public boolean signUpProcess(MemberDTO memberDTO) {

        //db에 이미 동일한 loginId를 가진 회원이 존재하는지 검사
        if (memberRepository.existsByLoginId(memberDTO.getLoginId())) { return false; }

        //db에 이미 동일한 nickname을 가진 회원이 존재하는지 검사
        if(memberRepository.existsByNickname(memberDTO.getNickname())) { return false; }

        //memberDTO를 entity로 변환 후 데이터베이스에 넣기
        Member member = new Member();

        member.setNickname(memberDTO.getNickname());
        member.setLoginId(memberDTO.getLoginId());
        member.setPassword(bCryptPasswordEncoder.encode(memberDTO.getPassword()));

        memberRepository.save(member);
        return true;
    }

    @Transactional
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        for(FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }
}
