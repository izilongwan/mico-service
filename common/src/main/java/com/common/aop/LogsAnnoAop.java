package com.common.aop;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

import com.common.aop.anno.LogsAnno;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Configuration
@Slf4j(topic = "cn")
public class LogsAnnoAop {
    @Pointcut("@annotation(com.common.aop.anno.LogsAnno)")
    public void anno() {

    }

    @AfterReturning("@annotation(logsAnno)")
    public void after(LogsAnno logsAnno) {
        log.debug("{}", logsAnno);
    }
}
