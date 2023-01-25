package com.common.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Objects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

import com.common.aop.anno.ValidatorAnno;
import com.common.aop.anno.ValidatorPropAnno;
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

    private <T> String checkValid(ValidatorAnno validatorAnno, String name, T val) {
        if (Objects.isNull(val)) {
            return String.format("属性[%s]的值为空", name);
        }

        String pattern = validatorAnno.value();
        ValidatorPropAnno[] propAnnos = validatorAnno.values();

        if (pattern.isEmpty()) {
            pattern = validatorAnno.pattern();
        }

        if (pattern.isEmpty() && propAnnos.length <= 0) {
            return null;
        }

        Class<T> clazz = (Class<T>) val.getClass();

        if (propAnnos.length > 0) {
            String checkPropAnno = checkPropAnno(propAnnos, clazz, val, name);
            return Objects.isNull(checkPropAnno) ? null : checkPropAnno;
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

    private <T> String checkPropAnno(ValidatorPropAnno[] propAnnos, Class<T> clazz, T val, String name) {
        for (ValidatorPropAnno propAnno : propAnnos) {
            try {
                Method method = clazz.getMethod(getMethodName(propAnno.prop()));
                Object invoke = method.invoke(val);
                String value = String.valueOf(invoke);

                if (!value.matches(propAnno.value())) {
                    String message = propAnno.message();
                    return message.isEmpty()
                            ? String.format("属性[%s.%s]的值不匹配", name, propAnno.prop())
                            : message;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }

        return null;
    }

    private String getMethodName(String prop) {
        String[] arr = { "is" };

        for (String item : arr) {
            if (!prop.startsWith(item)) {
                return String.format(
                        "get%s%s",
                        prop.substring(0, 1).toUpperCase(Locale.ENGLISH),
                        prop.substring(1));
            }
        }

        return prop;
    }

}
