package com.m3.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.common.entity.R;
import com.feignapi.client.UserClient;

@RestController
@RequestMapping("m3")
public class UserController {
    @Resource
    UserClient userClient;

    @GetMapping("")
    public R<R<String>> user() {
        // 服务的名称
        R<String> v = userClient.getInfo();

        System.out.println(v);

        return R.SUCCESS(v);
    }

    @GetMapping("{id}")
    public R<R<String>> id(@PathVariable Long id) {
        // 服务的名称
        R<String> v = userClient.getId(id);

        return R.SUCCESS(v);
    }
}
