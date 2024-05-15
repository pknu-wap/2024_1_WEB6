// OAuth2 인증 관련 실패를 위한 사용자 정의 예외 클래스
package com.web6.server.oauth2login.exception;

import org.springframework.security.core.AuthenticationException;

public class OAuth2AuthenticationProcessingException extends AuthenticationException {
    public OAuth2AuthenticationProcessingException(String msg) {
        super(msg);
    }
}