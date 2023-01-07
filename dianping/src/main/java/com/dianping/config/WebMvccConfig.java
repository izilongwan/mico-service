package com.dianping.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.dianping.interceptor.LoginInterceptor;
import com.dianping.interceptor.RefreshInterceptor;

@Configuration
public class WebMvccConfig extends WebMvcConfigurationSupport {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RefreshInterceptor());
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns("/dianping/user/cookie/**", "/dianping/user/test/**")
                .order(2);
    }

}
