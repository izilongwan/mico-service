package com.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

import com.common.aop.anno.NotNullAnno;
import com.common.entity.R;
import com.common.util.AopCheckUtil;
import com.google.common.base.Objects;

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
                        if (paramBo.getAnnotation() instanceof NotNullAnno &&
                                Objects.equal(paramBo.getValue(), null)) {
                            String checkValid = String.format("属性[%s]的值为空", paramBo.getName());
                            AopCheckUtil.throwException(checkValid);
                        }

                        return null;
                    });
            return proceedingJoinPoint.proceed();

        } catch (Throwable e) {
            return R.ERROR(e.getMessage());
        }

    }

}
