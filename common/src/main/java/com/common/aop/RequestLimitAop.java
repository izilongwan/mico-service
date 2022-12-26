package com.common.aop;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.common.aop.anno.RequestLimitAnno;

@Aspect
@Configuration
public class RequestLimitAop {
    String countKey = "count";
    String ipKey = "ip";
    String timestampKey = "timestamp";

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Pointcut("@annotation(com.common.aop.anno.RequestLimitAnno)")
    public void requestLimit() {

    }

    @Around("@annotation(requestLimitAnno)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, RequestLimitAnno requestLimitAnno) throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        ServletRequestAttributes req = ((ServletRequestAttributes) requestAttributes);

        HttpServletRequest request = req.getRequest();

        String requestURI = request.getRequestURI();
        String key = requestLimitAnno.keyPrefix() + requestURI;
        BoundHashOperations<String, Object, Object> boundHashOps = redisTemplate.boundHashOps(key);

        Object object = boundHashOps.get(countKey);

        if (Objects.equals(object, null)) {
            String remoteAddr = request.getRemoteAddr();

            boundHashOps.put(ipKey, remoteAddr);
            boundHashOps.put(countKey, 1);
            boundHashOps.put(timestampKey, System.currentTimeMillis() / 1000);

            boundHashOps.expire(requestLimitAnno.time(), TimeUnit.MILLISECONDS);
        } else {
            Integer count = ((Integer) boundHashOps.get(countKey));

            if (count > requestLimitAnno.value()) {
                throw new RuntimeException("访问次数过多, 请稍后再试");
            }

            boundHashOps.increment(countKey, 1);
            boundHashOps.put(timestampKey, System.currentTimeMillis() / 1000);
        }

        return proceedingJoinPoint.proceed();
    }
}
