package com.common.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.function.Function;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import com.common.bo.ParamBo;

public class AopCheckUtil {
    // 获取方法参数注解
    public static Object checkMethodParam(
            ProceedingJoinPoint proceedingJoinPoint,
            Function<ParamBo, Object> cb) {
        MethodSignature methodSignature = ((MethodSignature) proceedingJoinPoint.getSignature());
        // 获取方法
        Method method = methodSignature.getMethod();
        // 获取方法参数注解
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        // 获取方法参数名称
        String[] parameterNames = methodSignature.getParameterNames();
        // 获取方法参数值
        Object[] args = proceedingJoinPoint.getArgs();

        int i = 0;
        int length = parameterAnnotations.length;
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            // 获取方法参数名
            String paramName = parameterNames[i];
            // 获取方法参数值
            Object paramValue = args[i];

            for (Annotation parameterAnno : parameterAnnotation) {
                ParamBo methodParamBo = new ParamBo(paramName, paramValue, parameterAnno, i,
                        i == length - 1);
                cb.apply(methodParamBo);
            }

            i++;
        }

        return null;
    }

    // 获取类属性注解, 需使用try catch返回R.Error()
    public static Object checkField(
            ProceedingJoinPoint proceedingJoinPoint,
            Function<ParamBo, Object> cb) throws Exception {
        MethodSignature methodSignature = ((MethodSignature) proceedingJoinPoint.getSignature());
        Parameter[] parameters = methodSignature.getMethod().getParameters();
        // 获取方法参数值
        Object[] args = proceedingJoinPoint.getArgs();

        for (Parameter parameter : parameters) {
            Class<?> clazz = parameter.getType();
            Field[] declaredFields = clazz.getDeclaredFields();

            // 实例对象
            Object arg = Arrays.stream(args)
                    .filter(ar -> clazz.isAssignableFrom(ar.getClass()))
                    .findFirst()
                    .get();
            int i = 0;
            int length = declaredFields.length;

            for (Field field : declaredFields) {
                field.setAccessible(true);
                // boolean annotationPresent = field.isAnnotationPresent(ValidatorAnno.class);
                Annotation[] annos = field.getAnnotations();
                String name = field.getName();
                Object value = field.get(arg);

                for (Annotation anno : annos) {
                    ParamBo paramBo = new ParamBo(name, value, anno, i, i == length - 1, field);
                    cb.apply(paramBo);
                }

            }

        }

        return null;
    }

    public static void throwException(Object message) {
        if (message instanceof String) {
            throw new RuntimeException(((String) message), new Throwable("AOP"));
        }
    }

}
