package com.feignapi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.common.entity.R;
import com.feignapi.client.fallback.TestClientFallbackFactory;

@FeignClient(name = "test", path = "test", fallbackFactory = TestClientFallbackFactory.class)
public interface TestClient {
    @GetMapping("dept")
    R<Object> select();
}
