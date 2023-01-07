package com.m3;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan({ "com.m3.mapper" })
@ComponentScan(basePackages = { "com.m3", "com.feignapi.config" })
// @EnableFeignClients(clients = { UserClient.class })
@EnableFeignClients(basePackages = "com.feignapi.client")
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
