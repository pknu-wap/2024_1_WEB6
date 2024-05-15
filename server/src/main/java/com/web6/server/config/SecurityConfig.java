package com.web6.server.config;

import com.web6.server.jwt.JwtAuthorizationFilter;

import com.web6.server.oauth2login.service.CustomOAuth2UserService;
import com.web6.server.oauth2login.HttpCookieOAuth2AuthorizationRequestRepository;
import com.web6.server.oauth2login.handler.OAuth2AuthenticationFailureHandler;
import com.web6.server.oauth2login.handler.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

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
    public BCryptPasswordEncoder bCryptPasswordEncoder() { //암호화메서드
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/").permitAll()
                        //.requestMatchers("/mypage/**").hasRole("USER") //USER라는 role만 접근허용
                        .requestMatchers("/login-page", "/api/members/login-page", "/loginError","/sign-up", "/api/members/sign-up").anonymous()
                        .anyRequest().authenticated() //로그인한 사용자만 허용
                );

        http
                .formLogin((auth) -> auth
                        .loginPage("/login-page")
                        .loginProcessingUrl("/api/members/login-page")
                        .failureForwardUrl("/loginError")
                        .defaultSuccessUrl("/")
                );

//        http.httpBasic(AbstractHttpConfigurer::disable)


        http
                .logout((auth) -> auth
                        .logoutUrl("/api/members/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true) //로그아웃 후 세션 무효화
                );


        http
                .csrf((auth) -> auth.disable()); //추후 enable로 수정
        
        http
                .sessionManagement((auth) -> auth
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true)); //true면 초과시 새로운 로그인 차단, false면 초과시 기존 세션 하나 삭제
        
        http.sessionManagement((auth) -> auth
                .sessionFixation().changeSessionId()); //세션 고정 보호

        //소셜로그인
        http    .sessionManagement(sessions -> sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .oauth2Login(configure ->
                            configure
                                    .authorizationEndpoint(config -> config.authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository))
                                    .userInfoEndpoint(config -> config.userService(customOAuth2UserService))
                                    .successHandler(oAuth2AuthenticationSuccessHandler)
                                    .failureHandler(oAuth2AuthenticationFailureHandler)
                    );
        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /*@Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManager.class);
    }*/
}
