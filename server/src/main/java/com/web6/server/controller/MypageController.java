package com.web6.server.controller;

import com.web6.server.domain.Member;
import com.web6.server.dto.ApiResponse;
import com.web6.server.dto.MemberDetails;
import com.web6.server.dto.MemberEditDTO;
import com.web6.server.dto.WithdrawDTO;
import com.web6.server.repository.MemberRepository;
import com.web6.server.service.MemberService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MypageController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    public MypageController(MemberService memberService,  MemberRepository memberRepository) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
    }

    @GetMapping("/api/mypage")
    public ApiResponse<MemberEditDTO> MypageP(@AuthenticationPrincipal MemberDetails currentMember) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        Member member = memberRepository.findByLoginId(id);

        // 소셜 로그인 유저인지 확인
        if (member.getAccessToken() != null && !member.getAccessToken().isEmpty()) {

            MemberEditDTO editDTO = new MemberEditDTO();
            editDTO.setNickname(member.getNickname());
            editDTO.setLoginId(member.getLoginId());

            return new ApiResponse<>(true, "마이페이지 이동 성공", editDTO);
        }

        //마이페이지로 이동하면, 현재 로그인한 유저의 닉네임과 아이디 필드가 빈값이 아니도록
        MemberEditDTO editDTO = new MemberEditDTO();
        editDTO.setNickname(currentMember.getNickname());
        editDTO.setLoginId(currentMember.getUsername()); //오버라이딩 메서드라서 getUsername()이지만, loginId를 반환함

        return new ApiResponse<>(true, "마이페이지 이동 성공", editDTO);
    }

    @PostMapping("/api/mypage/duplicate")
    public ApiResponse<Void> CheckNicknameDuplicate(@RequestBody MemberEditDTO editDTO, @AuthenticationPrincipal MemberDetails currentMember) {
        String nickname = editDTO.getNickname();

        Map<String, String> error = new HashMap<>();
        if(!currentMember.getNickname().equals(nickname)) {
            //닉네임의 유효성 검사
            if(!memberService.isValidNickname(nickname)) {
                error.put("valid_nickname", "닉네임은 2~10자의 한글, 영문 대소문자, 숫자로 구성되어야 합니다.");

                return new ApiResponse<>(false, "마이페이지 닉네임 유효성 검사 탈락", error, null);
            }

            //닉네임의 중복 검사
            if(memberService.isDuplicatedNickname(nickname)) {
                error.put("duplicate", "이미 존재하는 닉네임입니다.");

                return new ApiResponse<>(false, "마이페이지 닉네임 중복 검사 탈락", error, null);
            }

            return new ApiResponse<>(true, "사용 가능한 닉네임", null);
        }
        error.put("change","변경 사항이 없습니다.");

        return new ApiResponse<>(false, "변경 사항이 없음", error, null);
    }

    @PostMapping("/api/mypage")
    public ApiResponse<MemberEditDTO>  MypageProcess(@RequestBody MemberEditDTO editDTO, @AuthenticationPrincipal MemberDetails currentMember) {
        // 소셜 로그인 유저인지 확인
        if (currentMember.getAccessToken() != null && !currentMember.getAccessToken().isEmpty()) {
            if (!editDTO.getCurrentPassword().isEmpty() || !editDTO.getNewPassword().isEmpty() || !editDTO.getConfirmPassword().isEmpty()) {
                Map<String, String> errorResult = new HashMap<>();
                errorResult.put("error", "소셜 로그인 유저는 비밀번호를 변경할 수 없습니다.");
                return new ApiResponse<>(false, "소셜 로그인 유저는 비밀번호를 변경할 수 없습니다.", errorResult, editDTO);
            }
        }
        //닉네임 변경 로직
        if(!currentMember.getNickname().equals(editDTO.getNickname()) && !editDTO.isNicknameChecked()){
            //닉네임에 변경 사항이 있는데, 중복확인 버튼을 누르지 않은 경우
            return new ApiResponse<>(false, "닉네임이 중복되는지 '중복확인'을 클릭하여 확인해주세요.", editDTO);
        }

        //비밀번호 변경 로직
        if (!editDTO.getCurrentPassword().isEmpty() || !editDTO.getNewPassword().isEmpty() || !editDTO.getConfirmPassword().isEmpty()) { //셋 중 하나라도 empty가 아니라면, 비밀번호 변경 의사가 있다는 것

            //현재 비밀번호가 일치하는 지 검사
            if (!memberService.checkPassword(editDTO.getLoginId(), editDTO.getCurrentPassword())) {
                Map<String, String> currentPwdError = new HashMap<>();
                currentPwdError.put("errorMessage", "현재 비밀번호가 일치하지 않습니다.");

                return new ApiResponse<>(false, "현재 비밀번호 불일치", currentPwdError, editDTO);
            }

            //새 비밀번호 유효성 검사
            if (!memberService.isValidPassword(editDTO.getNewPassword())) {
                Map<String, String> validResult = new HashMap<>();
                validResult.put("valid_password", "새 비밀번호는 8~16자의 영문 대소문자, 숫자, 특수문자를 모두 포함해야 합니다.");

                return new ApiResponse<>(false, "마이페이지 새 비밀번호 유효성 검사 탈락", validResult, editDTO);
            }

            //새 비밀번호와 새 비밀번호 확인의 입력 값이 일치하는 지 검사
            if (!editDTO.getNewPassword().equals(editDTO.getConfirmPassword())) {
                Map<String, String> confirmPwd = new HashMap<>();
                confirmPwd.put("confirm_password", "새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");

                return new ApiResponse<>(false, "마이페이지 새 비밀번호와 비밀번호 확인 불일치", confirmPwd, editDTO);

            }
            memberService.updatePassword(editDTO.getLoginId(), editDTO.getNewPassword());
        }
        memberService.updateNickname(editDTO.getLoginId(), editDTO.getNickname());

        /*
        * 변경된 세션 등록
        */
        /* Authentication authentication = authenticationManagerBean.authenticate(
                new UsernamePasswordAuthenticationToken(editDTO.getLoginId(), editDTO.getNewPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);*/

        return new ApiResponse<>(true, "회원정보 수정 완료", null);
        //성공하면, 새로운 세션을 받아오기 위해, 강제로 로그아웃 시켜야 함
    }

    //회원 탈퇴
    @DeleteMapping("/api/members/withdraw")
    public ApiResponse<Void> memberWithdraw(@RequestBody WithdrawDTO password) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String writerId = authentication.getName();

        if(!memberService.checkPassword(writerId, password.getConfirmPassword())) {
            return new ApiResponse<>(false, "비밀번호가 일치하지 않습니다.", null);
        }

        if(memberService.withdraw(writerId)){
            return new ApiResponse<>(true, "회원탈퇴 성공", null);
        }
        return new ApiResponse<>(false, "회원탈퇴 실패", null);
    }


}
