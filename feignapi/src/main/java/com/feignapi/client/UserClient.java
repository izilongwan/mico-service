package com.feignapi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.common.entity.R;

@FeignClient("eureka-m2")
public interface UserClient {
    @GetMapping("m2")
    R<String> getInfo();

    @GetMapping("m2/{id}")
    R<String> getId(@PathVariable("id") Long id);
}
