package com.web6.server.oauth2login.user;

import com.web6.server.oauth2login.exception.OAuth2AuthenticationProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OAuth2UserUnlinkManager {

    private final KakaoOAuth2UserUnlink kakaoOAuth2UserUnlink;

    public void unlink(OAuth2Provider provider, String accessToken) {
        if (OAuth2Provider.KAKAO.equals(provider)) {
            kakaoOAuth2UserUnlink.unlink(accessToken);
        } else {
            throw new OAuth2AuthenticationProcessingException(
                    "Unlink with " + provider.getRegistrationId() + " is not supported");
        }
    }
}