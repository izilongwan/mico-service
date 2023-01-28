package com.common.aop;

import java.lang.reflect.Field;
import java.util.Objects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

import com.common.aop.anno.NotNullAnno;
import com.common.entity.R;
import com.common.util.AopCheckUtil;

@Aspect
@Configuration
public class NotNullAnnoAop {
    @Pointcut("@annotation(com.common.aop.anno.NotNullAnno)")
    public void anno() {

    }

    @Pointcut("execution(public * com..controller.*Controller.*(..))")
    public void controller() {

    }

    @Around("anno() || controller()")
    public Object aroundCheckField(ProceedingJoinPoint proceedingJoinPoint) {
        try {
            AopCheckUtil.checkField(
                    proceedingJoinPoint,
                    paramBo -> {
                        Field field = paramBo.getField();

                        if (Objects.isNull(field)) {
                            return null;
                        }

                        if (field.isAnnotationPresent(NotNullAnno.class)) {
                            if (Objects.isNull(paramBo.getValue())) {
                                String checkValid = String.format("属性[%s]的值为空", paramBo.getName());
                                AopCheckUtil.throwException(checkValid);
                            }
                        }

                        return null;
                    });
            return proceedingJoinPoint.proceed();

        } catch (Throwable e) {
            return R.ERROR(e.getMessage());
        }

    }

}
