package com.common.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

import com.common.aop.anno.LogsAnno;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Configuration
@Slf4j
public class LogsAnnoAop {
    @Pointcut("@annotation(com.common.aop.anno.LogsAnno)")
    public void anno() {

    }

    @Before("anno()")
    public void before(JoinPoint joinPoint) {
        log.debug("before {}", joinPoint);
    }

    @Around("anno()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) {
        log.debug("around {}", proceedingJoinPoint);

        try {
            return proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    @AfterReturning("@annotation(logsAnno)")
    public void afterReturning(LogsAnno logsAnno) {
        log.debug("afterReturning {}", logsAnno);
    }
}
