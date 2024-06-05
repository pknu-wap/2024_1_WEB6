package com.web6.server.controller.loginHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");

        // 세션 ID를 포함한 응답 데이터 생성
        String sessionId = request.getSession().getId();
        response.getWriter().write(objectMapper.writeValueAsString(new LoginResponse(true, "로그인 성공", sessionId)));
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
