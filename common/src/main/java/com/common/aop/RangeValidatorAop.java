package com.common.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import com.common.aop.anno.RangeValidatorAnno;
import com.common.entity.R;

@Configuration
@Aspect
public class RangeValidatorAop {
    @Pointcut("execution(public * com..controller.*Controller.*(..))")
    public void range() {

    }

    @Pointcut("@annotation(com.common.aop.anno.RangeValidatorAnno)")
    public void anno() {

    }

    @Around("range()")
    public Object aroundCheckField(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = ((MethodSignature) proceedingJoinPoint.getSignature());

        Parameter[] parameters = methodSignature.getMethod().getParameters();

        Object[] args = proceedingJoinPoint.getArgs();

        for (Parameter parameter : parameters) {
            Class<?> clazz = parameter.getType();
            Field[] declaredFields = clazz.getDeclaredFields();

            Object arg = Arrays.stream(args).filter(ar -> clazz.isAssignableFrom(ar.getClass())).findFirst().get();

            for (Field field : declaredFields) {
                field.setAccessible(true);
                boolean annotationPresent = field.isAnnotationPresent(RangeValidatorAnno.class);

                if (annotationPresent) {
                    Object val = field.get(arg);

                    if (val != null) {
                        Integer value = (Integer) val;

                        RangeValidatorAnno rangeValidatorAnno = field.getAnnotation(RangeValidatorAnno.class);

                        long v = rangeValidatorAnno.value();

                        String name = field.getName();

                        if (v != 0 && value != v) {
                            return R.ERROR("[" + name + "]的属性值必须为: " + v);
                        }

                        long max = rangeValidatorAnno.max();
                        long min = rangeValidatorAnno.min();

                        if (value > max || value < min) {
                            return R.ERROR("[" + name + "]的属性值超出范围");
                        }
                    }
                }
            }

        }

        return proceedingJoinPoint.proceed();
    }

    @Around("range()")
    public Object aroundCheckParameter(ProceedingJoinPoint proceedingJoinPoint)
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
                    Long value = (Long) args[i];
                    String name = names[i];
                    long v = rangeValidatorAnno.value();

                    if (v != 0 && value != v) {
                        return R.ERROR("[" + name + "]的属性值必须为: " + v);
                    }

                    long max = rangeValidatorAnno.max();
                    long min = rangeValidatorAnno.min();

                    if (value > max || value < min) {
                        return R.ERROR("[" + name + "]的属性值超出范围");
                    }
                }
            }
        }

        return proceedingJoinPoint.proceed();
    }
}