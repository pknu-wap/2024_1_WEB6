/*
OAuth2 연결 끊기 기능 구현시 HTTP API 요청을 위해 사용.
RestTemplate 객체를 스프링 빈으로 등록하기 위한 설정 클래스.
 */
package com.web6.server.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }
}
