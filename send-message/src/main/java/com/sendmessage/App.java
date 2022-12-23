package com.sendmessage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.common.config.RedisConfig;

@SpringBootApplication
@ComponentScan(basePackages = { "com.sendmessage" }, basePackageClasses = { RedisConfig.class })
@MapperScan({ "com.sendmessage.mapper" })
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
