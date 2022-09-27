package com.eurekasvr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;

@EnableEurekaServer
@SpringBootApplication
@EnableCreateCacheAnnotation
@EnableMethodCache(basePackages = { "com.eureka" })
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
