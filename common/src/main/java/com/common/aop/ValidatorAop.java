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

import com.common.aop.anno.ValidatorAnno;
import com.common.entity.R;

@Configuration
@Aspect
public class ValidatorAop {
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
                boolean annotationPresent = annotation instanceof ValidatorAnno;

                if (annotationPresent) {
                    ValidatorAnno validatorAnno = ((ValidatorAnno) annotation);
                    String name = names[i];
                    Object value = args[i];

                    String msg = checkValid(validatorAnno, name, value);

                    if (msg != null) {
                        return R.ERROR(msg);
                    }
                }
            }
        }

        return proceedingJoinPoint.proceed();
    }

    @Pointcut("@annotation(com.common.aop.anno.ValidatorAnno)")
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
                boolean annotationPresent = field.isAnnotationPresent(ValidatorAnno.class);

                if (annotationPresent) {
                    ValidatorAnno validatorAnno = field.getAnnotation(ValidatorAnno.class);
                    Object value = field.get(arg);
                    String name = field.getName();

                    String msg = checkValid(validatorAnno, name, value);

                    if (msg != null) {
                        return R.ERROR(msg);
                    }
                }
            }

        }

        return proceedingJoinPoint.proceed();
    }

    private String checkValid(ValidatorAnno validatorAnno, String name, Object val) {
        if (val == null) {
            return String.format("属性值[%s]为空", name);
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
                    ? String.format("属性值[%s]不匹配", name)
                    : message;
        }

        return null;
    }

}
