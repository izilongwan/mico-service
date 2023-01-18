package com.common.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import com.common.aop.anno.RangeValidatorAnno;
import com.common.entity.R;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Aspect
@Slf4j
public class RangeValidatorAop {
    @Before("anno()")
    public void before(JoinPoint joinPoint) {
        log.debug("before {}", joinPoint);
    }

    @After("anno()")
    public void after(JoinPoint joinPoint) {
        log.debug("after {}", joinPoint);
    }

    @Pointcut("execution(public * com..controller.*Controller.*(..))")
    private void range() {

    }

    @Around("range() || anno()")
    private Object aroundCheckParameter(ProceedingJoinPoint proceedingJoinPoint)
            throws Throwable {
        // 参数属性值
        Object[] args = proceedingJoinPoint.getArgs();

        MethodSignature methodSignature = ((MethodSignature) proceedingJoinPoint.getSignature());

        // 参数属性名
        String[] names = methodSignature.getParameterNames();
        Method method = methodSignature.getMethod();

        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        for (int i = 0; i < args.length; i++) {
            Annotation[] parameterAnnotation = parameterAnnotations[i];

            for (Annotation annotation : parameterAnnotation) {
                boolean annotationPresent = annotation instanceof RangeValidatorAnno;

                if (annotationPresent) {
                    RangeValidatorAnno rangeValidatorAnno = ((RangeValidatorAnno) annotation);
                    String name = names[i];
                    Object value = args[i];

                    String msg = checkValid(rangeValidatorAnno, name, value);

                    if (msg != null) {
                        return R.ERROR(msg);
                    }
                }
            }
        }

        return proceedingJoinPoint.proceed();
    }

    @Pointcut("@annotation(com.common.aop.anno.RangeValidatorAnno)")
    private void anno() {

    }

    @Around("range() || anno()")
    private Object aroundCheckField(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = ((MethodSignature) proceedingJoinPoint.getSignature());

        Parameter[] parameters = methodSignature.getMethod().getParameters();

        Object[] args = proceedingJoinPoint.getArgs();

        for (Parameter parameter : parameters) {
            Class<?> clazz = parameter.getType();
            Field[] declaredFields = clazz.getDeclaredFields();

            // 实例对象
            Object arg = Arrays.stream(args)
                    .filter(ar -> clazz.isAssignableFrom(ar.getClass()))
                    .findFirst()
                    .get();

            for (Field field : declaredFields) {
                field.setAccessible(true);
                boolean annotationPresent = field.isAnnotationPresent(RangeValidatorAnno.class);

                if (annotationPresent) {
                    RangeValidatorAnno rangeValidatorAnno = field.getAnnotation(RangeValidatorAnno.class);
                    Object value = field.get(arg);
                    String name = field.getName();

                    String msg = checkValid(rangeValidatorAnno, name, value);

                    if (msg != null) {
                        return R.ERROR(msg);
                    }
                }
            }

        }

        return proceedingJoinPoint.proceed();
    }

    private String checkValid(RangeValidatorAnno rangeValidatorAnno, String name, Object val) {
        if (val == null) {
            return String.format("属性值[%s]为空", name);
        }

        long value = 0;

        if (val instanceof Integer) {
            value = ((Integer) val).longValue();
        } else if (val instanceof Long) {
            value = (Long) val;
        } else {
            return String.format("属性值[%s]类型错误", name);
        }

        long v = rangeValidatorAnno.value();

        if (v != 0 && value != v) {
            return String.format("属性值[%s]不匹配", name);
        }

        long max = rangeValidatorAnno.max();
        long min = rangeValidatorAnno.min();

        if (value > max || value < min) {
            return String.format("属性值[%s]超出范围", name);
        }

        return null;
    }

}
