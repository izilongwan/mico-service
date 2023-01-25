package com.common.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.common.entity.AppInfo;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableKnife4j
@EnableSwagger2WebMvc
@EnableSwagger2WebFlux
@ComponentScan(basePackages = "com.common.entity")
public class WebMvcSwaggerConfig extends WebMvcConfigurationSupport {
        @Resource
        AppInfo appInfo;

        // 静态资源访问
        @Override
        protected void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/doc.html")
                                .addResourceLocations("classpath:/META-INF/resources/");
                registry.addResourceHandler("/webjars/**")
                                .addResourceLocations("classpath:/META-INF/resources/webjars/");
                registry.addResourceHandler("/pages/**")
                                .addResourceLocations("classpath:/pages/");
        }

        @Bean
        public Docket docket() {
                String swaggerBasePackage = appInfo.getSwaggerBasePackage();
                String basePackage = swaggerBasePackage.isEmpty() ? "com.controller" : swaggerBasePackage;
                return new Docket(DocumentationType.SWAGGER_2)
                                .apiInfo(apiInfo())
                                .select()
                                .apis(RequestHandlerSelectors.basePackage(basePackage))
                                .paths(PathSelectors.any())
                                .build();
        }

        @Bean
        public ApiInfo apiInfo() {
                String name = appInfo.getName();
                Integer port = appInfo.getPort();
                return new ApiInfoBuilder()
                                .title(String.format("%s - [%s]", name, port))
                                .description(String.format("description: name=%s, port=%s", name, port))
                                .version("1.0")
                                .build();
        }
}
