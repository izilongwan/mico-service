package com.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.common.entity.R;

@Configuration
@Aspect
public class TimecostAop {
    @Value("${share.timestamp:timestamp}")
    String timestamp;

    @Pointcut("execution(public * com..controller.*Controller.*(..))")
    public void timecost() {

    }

    @Around("timecost()")
    @SuppressWarnings("unchecked")
    public Object timecostAround(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        long ts = System.currentTimeMillis();

        attrs.setAttribute(timestamp, ts, 0);

        Object data = pjp.proceed();

        if (data instanceof R) {
            R<Object> rs = (R<Object>) data;
            rs.setTimecost(System.currentTimeMillis() - ts);

            return rs;
        }

        return data;
    }
}
