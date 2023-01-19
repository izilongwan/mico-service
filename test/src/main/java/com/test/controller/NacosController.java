package com.test.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.common.entity.R;

@RestController
@RequestMapping("test/nacos")
@RefreshScope
public class NacosController {
    @Value("${pattern.format:}")
    private String pattern;

    @Value("${info.name:}")
    String name;

    @Value("${info.age:}")
    String age;

    @GetMapping("info")
    public R<String> info() {
        String baseInfo = String.format("name: %s, age: %s", name, age);
        return R.SUCCESS(baseInfo);
    }

    @GetMapping("time")
    public R<String> pattern() {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
        return R.SUCCESS(time);
    }
}
