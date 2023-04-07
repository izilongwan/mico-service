package com.feignapi.client.fallback;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import com.common.entity.R;
import com.feignapi.client.TestClient;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TestClientFallbackFactory implements FallbackFactory<TestClient> {

    @Override
    public TestClient create(Throwable cause) {
        return new TestClient() {

            @Override
            public R<Object> select() {
                log.error("[熔断处理] [{}] cause: {}, msg: {}", this.getClass().getName(), cause, cause.toString());
                return R.ERROR(String.format("[熔断处理] [%s] msg: %s", this.getClass().getName(), cause.toString()));
            }

        };

    }

}
