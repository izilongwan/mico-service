package com.test;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

import com.common.aop.TimecostAop;

@SpringBootApplication
@ComponentScan(basePackages = { "com.test" }, basePackageClasses = { TimecostAop.class })
// @ComponentScan({ "com.test", "com.common.aop" })
@MapperScan({ "com.test.mapper", "com.test.dto" })
@EnableFeignClients({ "com.feignapi.client" })
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
