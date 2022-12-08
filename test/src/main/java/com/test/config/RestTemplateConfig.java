package com.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    // 用来发送请求
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
