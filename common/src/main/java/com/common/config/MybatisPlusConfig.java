package com.common.config;

import java.time.LocalDateTime;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "cn")
@Configuration
public class MybatisPlusConfig implements MetaObjectHandler {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mPlusInterceptor = new MybatisPlusInterceptor();

        mPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());

        return mPlusInterceptor;
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("插入数据，更新时间 ===>");
        this.setFieldValByName("createTime", LocalDateTime.now().toString(), metaObject);
        this.setFieldValByName("updateTime", LocalDateTime.now().toString(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("更新数据，更新时间 ===>");
        this.setFieldValByName("updateTime", LocalDateTime.now().toString(), metaObject);
    }
}
