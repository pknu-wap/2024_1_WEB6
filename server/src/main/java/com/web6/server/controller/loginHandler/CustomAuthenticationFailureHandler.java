package com.web6.server.controller.loginHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        // 응답 데이터 생성
        response.getWriter().write(objectMapper.writeValueAsString(new LoginResponse(false, "로그인 실패", null)));
    }

    static class LoginResponse {
        private boolean success;
        private String message;
        private String sessionId;

        public LoginResponse(boolean success, String message, String sessionId) {
            this.success = success;
            this.message = message;
            this.sessionId = sessionId;
        }

        public boolean isSuccess() {
            return success;
        }
        public void setSuccess(boolean success) {
            this.success = success;
        }
        public String getMessage() {
            return message;
        }
        public void setMessage(String message) {
            this.message = message;
        }
        public String getSessionId() {
            return sessionId;
        }
        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }
    }
}
