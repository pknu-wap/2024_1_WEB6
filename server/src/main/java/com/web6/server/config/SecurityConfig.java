package com.web6.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() { //암호화메서드
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/login", "/api/members/login","/sign-up", "/api/members/sign-up").permitAll()
                        //.requestMatchers("/mypage/**").hasRole("USER") //USER라는 role만 접근허용
                        .anyRequest().authenticated() //로그인한 사용자만 허용
                );

        http
                .formLogin((auth) -> auth
                        .loginPage("/login")
                        .loginProcessingUrl("/api/members/login")
                        .failureUrl("/sign-up") //회원가입 실패 시 리다이렉트할 URL 지정
                        .permitAll()
                );

        http
                .csrf((auth) -> auth.disable()); //추후 enable로 수정


        return http.build();
    }
}
