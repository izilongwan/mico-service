package com.m3;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;

@SpringBootApplication
@MapperScan({ "com.m3.mapper" })
@ServletComponentScan
@EnableTransactionManagement
@EnableAsync
// @EnableFeignClients(clients = { UserClient.class })
@EnableFeignClients(basePackages = "com.feignapi.client")
@EnableCreateCacheAnnotation
@EnableMethodCache(basePackages = { "com.m3" })
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
