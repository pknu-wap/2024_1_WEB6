package com.web6.server.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @Column(name = "NICKNAME", nullable = false, length = 10, unique = true)
    private String nickname;

    @Column(name = "LOGIN_ID", nullable = false, length = 50, unique = true)
    private String loginId;

    @Column(name = "PASSWORD", nullable = false, length = 1000)
    private String password;

    @Column(name = "kakao_id")
    private String kakaoId;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    // Constructors, getters and setters
    public Member() {
    }

    public Member(String nickname, String loginId, String password, String kakaoId, String accessToken, String refreshToken) {
        this.nickname = nickname;
        this.loginId = loginId;
        this.password = password;
        this.kakaoId = kakaoId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKakaoId() {
        return kakaoId;
    }

    public void setKakaoId(String kakaoId) {
        this.kakaoId = kakaoId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
