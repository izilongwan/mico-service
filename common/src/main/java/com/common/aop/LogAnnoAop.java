package com.common.aop;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

import com.common.aop.anno.LogAnno;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Configuration
@Slf4j
public class LogAnnoAop {
    @AfterReturning("@annotation(logAnno)")
    public void doo(LogAnno logAnno) {
        log.debug("{}", logAnno);
    }

    @Pointcut("@annotation(com.common.aop.anno.LogAnno)")
    public void anno() {

    }
}
