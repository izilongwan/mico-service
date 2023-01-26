package com.common.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
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
    @Pointcut("@annotation(logsAnno)")
    public void anno(LogsAnno logsAnno) {

    }

    @Before("anno(logsAnno)")
    public void before(JoinPoint joinPoint, LogsAnno logsAnno) {
        log.debug("before {} {}", joinPoint, logsAnno);
    }

    @After("anno(logsAnno)")
    public void after(JoinPoint joinPoint, LogsAnno logsAnno) {
        log.debug("after {}", joinPoint);
    }

    @Around("anno(logsAnno)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, LogsAnno logsAnno) {
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
