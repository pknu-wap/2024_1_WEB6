package com.web6.server.service;


import com.web6.server.domain.*;
import com.web6.server.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ReviewRepository reviewRepository;
    private final ReviewArticleRepository reviewArticleRepository;
    private final CommentReviewRepository commentReviewRepository;
    private final CommentRepository commentRepository;
    private final ReviewService reviewService;

    @Autowired
    public MemberService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ReviewRepository reviewRepository, ReviewArticleRepository reviewArticleRepository, CommentReviewRepository commentReviewRepository, CommentRepository commentRepository, ReviewService reviewService) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.reviewRepository = reviewRepository;
        this.reviewArticleRepository = reviewArticleRepository;
        this.commentReviewRepository = commentReviewRepository;
        this.commentRepository = commentRepository;
        this.reviewService = reviewService;
    }


    //수정된 닉네임의 유효성 검사
    public boolean isValidNickname(String nickname) {
        String pattern = "^[ㄱ-ㅎ가-힣a-zA-Z0-9]{2,10}$";

        return Pattern.matches(pattern, nickname);
    }

    //수정된 닉네임의 중복 검사
    public boolean isDuplicatedNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    @Transactional
    public void updateNickname(String loginId, String newNickname) {
        Member member = memberRepository.findByLoginId(loginId);

        member.setNickname(newNickname);

        /*Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(member.getNickname(), member.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);*/
    }


    //입력한 현재 비밀번호가 일치하는 지 검사
    public boolean checkPassword(String loginId, String currentPassword) {
        Member member = memberRepository.findByLoginId(loginId);

        return bCryptPasswordEncoder.matches(currentPassword, member.getPassword());
    }

    //입력한 새 비밀번호의 유효성 검사
    public boolean isValidPassword(String newPassword) {
        String pattern = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}";

        return Pattern.matches(pattern, newPassword);
    }

    //디비에 수정된 비밀번호 저장
    @Transactional
    public void updatePassword(String loginId, String newPassword) {
        Member member = memberRepository.findByLoginId(loginId);

        member.setPassword(bCryptPasswordEncoder.encode(newPassword));

        /*Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(member.getNickname(), member.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);*/
    }

    //회원 탈퇴
    @Transactional
    public boolean withdraw(String writerId) {
        Member member = memberRepository.findByLoginId(writerId);

        //대댓글 삭제
        List<Comment_Review> commentReviews = commentReviewRepository.findByCommentWriter(member);
        for(Comment_Review commentReview : commentReviews) {
            Comment comment = commentReview.getComment();
            commentReviewRepository.delete(commentReview);
            commentRepository.delete(comment);
        }

        //리뷰 삭제
        List<Review_Article> reviewArticles = reviewArticleRepository.findByReviewWriterOrderByReviewCreateDateDesc(member);
        for(Review_Article reviewArticle : reviewArticles) {
            Review review = reviewArticle.getReview();
            reviewService.deleteReview(review.getId());
        }

        //회원 삭제
        memberRepository.delete(member);

        return true;
    }

}
