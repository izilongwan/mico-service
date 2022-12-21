package com.common.aop;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.common.aop.anno.LogAnno;

@Aspect
@Configuration
public class LogAnnoAop {
    @Value("${key.employee:employee}")
    String employee;

    @Value("${key.updated-employee:updated-employee}")
    String updatedEmployee;

    @AfterReturning("@annotation(logAnno)")
    public void doo(LogAnno logAnno) {
        System.out.println(logAnno);
    }

    @Pointcut("@annotation(com.aop.inter.LogAnno)")
    public void anno() {

    }
}
