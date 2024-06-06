/*
processOAuth2User에서 리턴하는 인증 객체는 OAuth2User 인터페이스임.
이 인터페이스의 사용자 정의 구현체 클래스
 */
package com.web6.server.oauth2login.service;

import com.web6.server.oauth2login.user.OAuth2UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class OAuth2UserPrincipal implements OAuth2User, UserDetails {

    private final OAuth2UserInfo userInfo;

    public OAuth2UserPrincipal(OAuth2UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return userInfo.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return userInfo.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getName() {
        return userInfo.getEmail();
    }

    public OAuth2UserInfo getUserInfo() {
        return userInfo;
    }

    }