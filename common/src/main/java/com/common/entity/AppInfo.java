package com.common.entity;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Getter
@RefreshScope
@Configuration
public class AppInfo implements Serializable {
    @Value("${spring.application.name:}")
    String name;

    @Value("${server.port:}")
    Integer port;

    @Value("${swagger-config.base-package:}")
    String swaggerBasePackage;
}
