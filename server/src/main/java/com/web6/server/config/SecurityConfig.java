package com.web6.server.config;

import com.web6.server.controller.loginHandler.CustomAuthenticationFailureHandler;
import com.web6.server.controller.loginHandler.CustomAuthenticationSuccessHandler;
import com.web6.server.jwt.JwtAuthorizationFilter;

import com.web6.server.oauth2login.service.CustomOAuth2UserService;
import com.web6.server.oauth2login.HttpCookieOAuth2AuthorizationRequestRepository;
import com.web6.server.oauth2login.handler.OAuth2AuthenticationFailureHandler;
import com.web6.server.oauth2login.handler.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/main", "/api/loginSuccess", "/oauth2/**").permitAll()
                        .requestMatchers("/login-page", "/api/members/login-page", "/api/loginError", "/sign-up", "/api/members/sign-up").anonymous()
                        .requestMatchers(("/movies/findAll")).permitAll() //movies/findAll 경로에 대해 모든 사용자 접근 허용
                        .requestMatchers(("/movies/search")).permitAll()  //movies/search 경로에 대해 모든 사용자 접근 허용
                        .requestMatchers(("/OrderByGradeCountDesc")).permitAll()  //OrderByGradeCountDesc 경로에 대해 모든 사용자 접근 허용
                        .requestMatchers(("/movies/search/json")).permitAll() //movies/search/json 경로에 대해 모든 사용자 접근 허용
                        .requestMatchers(("/movies/latest")).permitAll()  //movies/latest 경로에 대해 모든 사용자 접근 허용
                        .requestMatchers("/api/movies/{movieId}/{movieSeq}/reviewsCommentCnt", "/api/movies/{movieId}/{movieSeq}/reviewsLatest").permitAll()
                        .requestMatchers(("/movies/detail/{movieId}/{movieSeq}")).permitAll()  ///movies/detail/{movieId}/{movieSeq} 경로에 대해 모든 사용자 접근 허용
                        .anyRequest().authenticated()
                );

        http
                .formLogin((auth) -> auth
                        .loginPage("/login-page")
                        .loginProcessingUrl("/api/members/login-page")
                        .successHandler(new CustomAuthenticationSuccessHandler()) //성공 핸들러 설정
                        .failureHandler(new CustomAuthenticationFailureHandler()) //실패 핸들러 설정
                );

        http.httpBasic(AbstractHttpConfigurer::disable);

        http
                .logout((auth) -> auth //일반 로그아웃
                        .logoutUrl("/api/members/logout-page")
                        .logoutSuccessUrl("/api/main")
                        .invalidateHttpSession(true)
                        .deleteCookies("accessToken", "refreshToken")
                        .deleteCookies("JSESSIONID")
                );

        http
                .csrf((auth) -> auth.disable());

        http
                .sessionManagement((auth) -> auth
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true));

        http.sessionManagement((auth) -> auth
                .sessionFixation().changeSessionId());

        // 소셜 로그인

        http.oauth2Login(configure ->
                configure
                        .authorizationEndpoint(config -> config.authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository))
                        .userInfoEndpoint(config -> config.userService(customOAuth2UserService))
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .failureHandler(oAuth2AuthenticationFailureHandler)
        );


        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setSameSite("None"); // SameSite 설정을 None으로 설정
        serializer.setUseSecureCookie(true); // Secure 속성을 설정 (HTTPS에서만 작동)
        return serializer;
    }
}
