package com.common.aop;

import java.lang.annotation.Annotation;
import java.util.Objects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

import com.common.aop.anno.ValidatorAnno;
import com.common.entity.R;
import com.common.util.AopCheckUtil;

@Configuration
@Aspect
public class ValidatorAop {
    @Pointcut("execution(public * com..controller.*Controller.*(..))")
    private void range() {

    }

    @Around("range() || anno()")
    private Object aroundCheckMethodParam(ProceedingJoinPoint proceedingJoinPoint)
            throws Throwable {
        AopCheckUtil.checkMethodParam(
                proceedingJoinPoint,
                paramBo -> {
                    Annotation annotation = paramBo.getAnnotation();
                    boolean annotationPresent = annotation instanceof ValidatorAnno;

                    if (annotationPresent) {
                        ValidatorAnno validatorAnno = ((ValidatorAnno) annotation);

                        String checkValid = checkValid(validatorAnno, paramBo.getName(), paramBo.getValue());
                        AopCheckUtil.throwException(checkValid);
                    }

                    return null;
                });

        return proceedingJoinPoint.proceed();
    }

    @Pointcut("@annotation(com.common.aop.anno.ValidatorAnno)")
    private void anno() {

    }

    @Around("range() || anno()")
    private Object aroundCheckField(ProceedingJoinPoint proceedingJoinPoint) {
        try {
            AopCheckUtil.checkField(
                    proceedingJoinPoint,
                    paramBo -> {
                        Annotation annotation = paramBo.getAnnotation();
                        if (annotation instanceof ValidatorAnno) {
                            String checkValid = checkValid((ValidatorAnno) annotation, paramBo.getName(),
                                    paramBo.getValue());
                            AopCheckUtil.throwException(checkValid);
                        }

                        return null;
                    });
            return proceedingJoinPoint.proceed();

        } catch (Throwable e) {
            return R.ERROR(e.getMessage());
        }

    }

    private String checkValid(ValidatorAnno validatorAnno, String name, Object val) {
        if (Objects.isNull(val)) {
            return String.format("属性[%s]的值为空", name);
        }

        String pattern = validatorAnno.value();

        if (pattern.isEmpty()) {
            pattern = validatorAnno.pattern();
        }

        if (pattern.isEmpty()) {
            return null;
        }

        String value = String.valueOf(val);

        if (!value.matches(pattern)) {
            String message = validatorAnno.message();
            return message.isEmpty()
                    ? String.format("属性[%s]的值不匹配", name)
                    : message;
        }

        return null;
    }

}
