package com.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

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
public class WebMvcConfig extends WebMvcConfigurationSupport {
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
                return new Docket(DocumentationType.SWAGGER_2)
                                .apiInfo(apiInfo())
                                .select()
                                .apis(RequestHandlerSelectors.basePackage("com.controller"))
                                .paths(PathSelectors.any())
                                .build();
        }

        @Bean
        public ApiInfo apiInfo() {
                return new ApiInfoBuilder()
                                .title("API")
                                .description("description")
                                .version("1.0")
                                .build();
        }
}
