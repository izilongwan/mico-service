package com.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

@Configuration
public class MybatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mPlusInterceptor = new MybatisPlusInterceptor();

        mPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());

        return mPlusInterceptor;
    }
}
