// 카카오 로그인 수행

package com.web6.server.oauth2login.handler;

import com.web6.server.oauth2login.user.KakaoOAuth2UserUnlink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.web6.server.domain.Member;
import com.web6.server.dto.MemberDTO;
import com.web6.server.domain.repository.MemberRepository;
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
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.IOException;
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
//    private static final Logger logger = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);

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
                existingMember.setAccessToken(principal.getUserInfo().getAccessToken()); //액세스 토큰을 받아옴
                existingMember.setRefreshToken(tokenProvider.createToken(authentication)); //리프레쉬 토큰을 생성함
                memberRepository.save(existingMember);
                return UriComponentsBuilder.fromUriString(targetUrl)
                        // TODO : 나중에 지우기 (URI에 노출됨)
                        .queryParam("access_token", existingMember.getAccessToken())
                        .queryParam("refresh_token", existingMember.getRefreshToken())
                        .build().toUriString();
            } else {
                // 기존 member 테이블의 LOGIN_ID와 email이 불일치하는 경우 -> 새로 회원가입 해야 함 (일반 회원가입으로 이동?)
                return UriComponentsBuilder.fromUriString("http://localhost:8080/sign-up")
                        .build().toUriString();
            }

        } else if ("unlink".equalsIgnoreCase(mode)) { // 로그아웃

            String accessToken = principal.getUserInfo().getAccessToken();
            String refreshToken = tokenProvider.createToken(authentication);
            OAuth2Provider provider = principal.getUserInfo().getProvider();

            // 액세스 토큰 삭제
            //log.info("num check: {}", num);
            Member member = memberRepository.findByAccessToken(accessToken);
            if (member != null) {
                memberRepository.delete(member);
            }

            // 해당하는 회원의 리프레시 토큰을 삭제
            //log.info("num check: {}", num);
            Member memberByRefreshToken = memberRepository.findByRefreshToken(refreshToken);
            if (memberByRefreshToken != null) {
                memberRepository.delete(memberByRefreshToken);
            }
            // OAuth2 공급자와의 연결 끊기 API 호출
            //KakaoOAuth2UserUnlink.unlink(accessToken);
            // OAuth2UserUnlinkManager를 통해 OAuth2 공급자와 연결된 액세스 토큰 및 리프레시 토큰을 삭제
            oAuth2UserUnlinkManager.unlink(provider, accessToken);

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

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}
