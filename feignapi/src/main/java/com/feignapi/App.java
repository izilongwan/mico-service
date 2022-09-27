package com.feignapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;

@SpringBootApplication
@EnableFeignClients
@EnableCreateCacheAnnotation
@EnableMethodCache(basePackages = { "com.feignapi" })
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
