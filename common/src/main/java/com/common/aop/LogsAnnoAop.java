package com.common.aop;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

import com.common.aop.anno.LogsAnno;

@Aspect
@Configuration
public class LogsAnnoAop {
    @Pointcut("@annotation(com.aop.anno.LogsAnno)")
    public void anno() {

    }

    @AfterReturning("@annotation(logsAnno)")
    public void after(LogsAnno logsAnno) {
        System.out.println(logsAnno);
    }
}
