package com.web6.server.controller;

import com.web6.server.dto.MemberDetails;
import com.web6.server.dto.MemberEditDTO;
import com.web6.server.repository.MemberRepository;
import com.web6.server.service.MemberService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MypageController {

    private final MemberService memberService;

    public MypageController(MemberService memberService, MemberRepository memberRepository) {
        this.memberService = memberService;
    }

    @GetMapping("/mypage")
    public String MypageP(@AuthenticationPrincipal MemberDetails currentMember, Model model) {

        //마이페이지로 이동하면, 현재 로그인한 유저의 닉네임과 아이디 필드가 빈값이 아니도록
        MemberEditDTO editDTO = new MemberEditDTO();
        editDTO.setNickname(currentMember.getNickname());
        editDTO.setLoginId(currentMember.getUsername()); //오버라이딩 메서드라서 getUsername()이지만, loginId를 반환함

        model.addAttribute("currentMember",editDTO);

        return "mypage";
    }

    @PostMapping("/api/mypage")
    public String MypageProcess(MemberEditDTO editDTO, @AuthenticationPrincipal MemberDetails currentMember, Model model) {

        //닉네임 변경 로직
        if(!currentMember.getNickname().equals(editDTO.getNickname())){
            model.addAttribute("currentMember", editDTO);
            model.addAttribute("editMember", editDTO);

            //닉네임의 유효성 검사
            if(!memberService.isValidNickname(editDTO.getNickname())) {
                model.addAttribute("valid_nickname", "닉네임은 2~10자의 한글, 영문 대소문자, 숫자로 구성되어야 합니다.");

                return "mypage";
            }

            //닉네임의 중복 검사
            if(memberService.isDuplicatedNickname(editDTO.getNickname())){
                model.addAttribute("duplicate", "이미 존재하는 닉네임입니다.");

                return "mypage";
            }

        }

        //비밀번호 변경 로직
        if (!editDTO.getCurrentPassword().isEmpty() || !editDTO.getNewPassword().isEmpty() || !editDTO.getConfirmPassword().isEmpty()) { //셋 중 하나라도 empty가 아니라면, 비밀번호 변경 의사가 있다는 것
            model.addAttribute("currentMember", editDTO);
            model.addAttribute("editMember", editDTO);

            //현재 비밀번호가 일치하는 지 검사
            if (!memberService.checkPassword(editDTO.getLoginId(), editDTO.getCurrentPassword())) {
                model.addAttribute("errorMessage", "현재 비밀번호가 일치하지 않습니다.");
                return "mypage";
            }

            //새 비밀번호 유효성 검사
            if (!memberService.isValidPassword(editDTO.getNewPassword())) {
                model.addAttribute("valid_password", "비밀번호는 8~16자의 영문 대소문자, 숫자, 특수문자를 모두 포함해야 합니다.");
                return "mypage";
            }

            //새 비밀번호와 새 비밀번호 확인의 입력 값이 일치하는 지 검사
            if (!editDTO.getNewPassword().equals(editDTO.getConfirmPassword())) {
                model.addAttribute("confirm_password", "새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
                return "mypage";
            }
        }
        memberService.updateNickname(editDTO.getLoginId(), editDTO.getNickname());
        memberService.updatePassword(editDTO.getLoginId(), editDTO.getNewPassword());

        /*
        * 변경된 세션 등록
        */
       /* Authentication authentication = authenticationManagerBean.authenticate(
                new UsernamePasswordAuthenticationToken(editDTO.getLoginId(), editDTO.getNewPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);*/



        return "redirect:/mypage"; //실패든, 성공이든 mypage로 이동
        //리다이렉트하면 필드가 빈값은 아니지만, 이전 세션 값 적용됨 -> 계속 리다이렉트로 쓸 거면, 세션에 저장된 값을 set 해주거나, 세션 다시 받아오기 등등 해결방법 모색
        //반면에, 그냥 페이지 이동이면, 필드가 빈값으로 뜸. 왤까 -> model 써서 다시 필드 채우던가 음
    }


}
