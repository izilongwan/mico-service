package com.feignapi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.common.entity.R;

@FeignClient("test")
public interface TestClient {
    @GetMapping("test/dept")
    R<Object> select();
}
