/*
서비스 별로 다른 구조를 통합하기 위한 인터페이스를 정의.
다른 소셜로그인도 추가할 경우 필요
 */
package com.web6.server.oauth2login.user;

import java.util.Map;

public interface OAuth2UserInfo {

    OAuth2Provider getProvider();

    String getAccessToken();

    Map<String, Object> getAttributes();

    String getId();

    String getEmail();

    String getName();

    String getFirstName();

    String getLastName();

    String getKakaoNickname();

    String getProfileImageUrl();
}