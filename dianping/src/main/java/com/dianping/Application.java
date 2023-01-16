package com.dianping;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.common.advice.ExceptionHandlerAdvice;
import com.common.config.MybatisPlusConfig;
import com.common.config.RedisConfig;

@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
@ComponentScan(basePackages = { "com.dianping" }, basePackageClasses = {
		ExceptionHandlerAdvice.class,
		MybatisPlusConfig.class,
		RedisConfig.class
})
@MapperScan({ "com.dianping.mapper", "com.dianping.dto" })
@EnableFeignClients({ "com.feignapi.client" })
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
