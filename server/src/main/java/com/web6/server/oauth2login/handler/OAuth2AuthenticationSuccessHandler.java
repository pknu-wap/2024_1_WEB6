// 카카오 로그인 수행

package com.web6.server.oauth2login.handler;

import com.web6.server.domain.Member;
import com.web6.server.repository.MemberRepository;
import com.web6.server.jwt.TokenProvider;
import com.web6.server.oauth2login.HttpCookieOAuth2AuthorizationRequestRepository;
import com.web6.server.oauth2login.service.OAuth2UserPrincipal;
import com.web6.server.oauth2login.user.OAuth2Provider;
import com.web6.server.oauth2login.user.OAuth2UserUnlinkManager;
import com.web6.server.oauth2login.util.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.web6.server.oauth2login.HttpCookieOAuth2AuthorizationRequestRepository.MODE_PARAM_COOKIE_NAME;
import static com.web6.server.oauth2login.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final OAuth2UserUnlinkManager oAuth2UserUnlinkManager;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String targetUrl;

        targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }



    @Transactional
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {

        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);


        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        String mode = CookieUtils.getCookie(request, MODE_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse("");

        OAuth2UserPrincipal principal = getOAuth2UserPrincipal(authentication);

        if (principal == null) {
            return UriComponentsBuilder.fromUriString(targetUrl)
                    .queryParam("error", "Login failed")
                    .build().toUriString();
        }

        if ("login".equalsIgnoreCase(mode)) {
            // 기존 member 테이블의 LOGIN_ID와 email이 동일한 경우
            Member existingMember = memberRepository.findByLoginId(principal.getUserInfo().getEmail());
            //db에 이미 동일한 loginId를 가진 회원이 존재하는지 검사
            if (existingMember != null) {
                existingMember.setKakaoId(principal.getUserInfo().getId()); //카카오 Id 값을 받아옴
                existingMember.setAccessToken(tokenProvider.createToken(authentication)); //액세스 토큰을 받아옴
                existingMember.setRefreshToken(tokenProvider.createToken(authentication)); //리프레쉬 토큰을 생성함
                memberRepository.save(existingMember); // 받아온 토큰을 DB에 저장
                // 응답 헤더에 토큰 설정
                String accessToken = existingMember.getAccessToken();
                String refreshToken = existingMember.getRefreshToken();

                return UriComponentsBuilder.fromUriString(targetUrl)
                        .queryParam("access_token", accessToken)
                        .queryParam("refresh_token", refreshToken)
                        .build().toUriString();

            } else {
                // 기존 member 테이블의 LOGIN_ID와 email이 불일치하는 경우 -> 새로 회원가입 해야 함 (일반 회원가입으로 이동?)
                return UriComponentsBuilder.fromUriString("http://localhost:8080/sign-up")
                        .build().toUriString();
            }

        } else if ("unlink".equalsIgnoreCase(mode)) {
        // 로그아웃 처리
        String accessToken = principal.getUserInfo().getAccessToken();
        OAuth2Provider provider = principal.getUserInfo().getProvider();

        // accessToken을 사용하여 OAuth2 공급자와의 연동 해제
        oAuth2UserUnlinkManager.unlink(provider, accessToken);

        // 현재 사용자의 인증 정보를 SecurityContext에서 제거
        SecurityContextHolder.clearContext();
        request.getSession().invalidate();

        // 쿠키에서 refreshToken 제거
        clearRefreshTokenCookie(response);
//        clearAccessTokenCookie(response);

        String kakaoId = principal.getUserInfo().getId();
        Member member = memberRepository.findByKakaoId(kakaoId);
            if (member != null) {
            member.setAccessToken(null);
            member.setRefreshToken(null);
            memberRepository.save(member);
        }

//        return "redirect:" + logoutRedirectUrl;
//    }
//
//        return UriComponentsBuilder.fromUriString(targetUrl)
//                .queryParam("error", "Login failed")
//                .build().toUriString();
        return UriComponentsBuilder.fromUriString(targetUrl)
                .build().toUriString();
    }

    return UriComponentsBuilder.fromUriString(targetUrl)
            .queryParam("error", "Login failed")
            .build().toUriString();
}

    private OAuth2UserPrincipal getOAuth2UserPrincipal(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2UserPrincipal) {
            return (OAuth2UserPrincipal) principal;
        }
        return null;
    }

    protected void clearRefreshTokenCookie(HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", null); // 쿠키 이름은 실제 사용하는 이름으로 변경
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge(0); // 쿠키를 즉시 만료시킴
        response.addCookie(refreshTokenCookie);
    }

    protected void clearAccessTokenCookie(HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie("accessToken", null); // 쿠키 이름은 실제 사용하는 이름으로 변경
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge(0); // 쿠키를 즉시 만료시킴
        response.addCookie(refreshTokenCookie);
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}
