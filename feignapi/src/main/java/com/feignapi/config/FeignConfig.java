package com.feignapi.config;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.common.base.Objects;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Configuration
public class FeignConfig {
    /**
     * 转发请求头信息
     *
     * @return
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {

            @Override
            public void apply(RequestTemplate template) {
                ServletRequestAttributes attrs = ((ServletRequestAttributes) RequestContextHolder
                        .getRequestAttributes());

                if (attrs != null) {
                    HttpServletRequest request = attrs.getRequest();
                    Enumeration<String> headerNames = request.getHeaderNames();

                    if (headerNames != null) {
                        while (headerNames.hasMoreElements()) {
                            String key = headerNames.nextElement();

                            if (!Objects.equal("host", key)) {
                                String value = request.getHeader(key);
                                template.header(key, value);
                            }
                        }
                    }
                }
            }
        };
    }
}
