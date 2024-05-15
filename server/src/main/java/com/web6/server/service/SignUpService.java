// 일반 회원가입
package com.web6.server.service;

import com.web6.server.domain.Member;
import com.web6.server.repository.MemberRepository;
import com.web6.server.dto.MemberDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import java.util.HashMap;
import java.util.Map;

@Service
public class SignUpService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public SignUpService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /*
     * 중복 검사
     */
    public String duplicateHandling(MemberDTO memberDTO) {
        String duplicateMessage = "";

        //db에 이미 동일한 nickname을 가진 회원이 존재하는지 검사
        if(memberRepository.existsByNickname(memberDTO.getNickname())) {
            duplicateMessage = "이미 존재하는 닉네임입니다.";
        }

        //db에 이미 동일한 loginId를 가진 회원이 존재하는지 검사
        if (memberRepository.existsByLoginId(memberDTO.getLoginId())) {
            duplicateMessage = "이미 존재하는 이메일입니다.";
        }

        return duplicateMessage;
    }

    /*
    * 유효성 검사
    */
    @Transactional
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        for(FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }

    /*
     * 데이터베이스에 추가
     */
    public void signUpProcess(MemberDTO memberDTO) {
        //memberDTO를 entity로 변환 후 데이터베이스에 넣기
        Member member = new Member();

        member.setNickname(memberDTO.getNickname());
        member.setLoginId(memberDTO.getLoginId());
        member.setPassword(bCryptPasswordEncoder.encode(memberDTO.getPassword()));

        memberRepository.save(member);
    }
}
