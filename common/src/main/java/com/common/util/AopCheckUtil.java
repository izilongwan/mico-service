package com.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.function.Function;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import com.common.advice.ExceptionHandlerAdvice;
import com.common.bo.ParamBo;
import com.common.entity.R;

@SuppressWarnings("unchecked")
public class AopCheckUtil {
    // 获取方法参数注解
    public static <T> Object checkMethodParam(
            ProceedingJoinPoint proceedingJoinPoint,
            Function<ParamBo<T>, Object> cb) {
        MethodSignature methodSignature = ((MethodSignature) proceedingJoinPoint.getSignature());
        // 获取方法
        Method method = methodSignature.getMethod();
        // 方法参数集合
        Parameter[] parameters = method.getParameters();
        // 获取方法参数值
        Object[] args = proceedingJoinPoint.getArgs();
        int i = 0;

        for (Parameter parameter : parameters) {
            // 获取方法参数名称
            String name = parameter.getName();
            // 获取方法参数值
            T value = (T) args[i];

            ParamBo<T> methodParamBo = new ParamBo<>(name, value, parameter);
            cb.apply(methodParamBo);
            i++;
        }

        return null;
    }

    // 获取类属性注解, 需使用try catch返回捕获异常
    public static <T> Object checkField(
            ProceedingJoinPoint proceedingJoinPoint,
            Function<ParamBo<T>, Object> cb) throws Exception {
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

            for (Field field : declaredFields) {
                // 强制获取成员信息
                field.setAccessible(true);
                // 获取成员参数名称
                String name = field.getName();
                // 获取成员参数值
                T value = (T) field.get(arg);

                ParamBo<T> paramBo = new ParamBo<>(name, value, parameter, field);
                cb.apply(paramBo);
            }
        }

        return null;
    }

    public static void runtimeException(Object message) {
        if (message instanceof String) {
            throw new RuntimeException(((String) message));
        }
    }

    public static R<Exception> error(Exception e) {
        return ExceptionHandlerAdvice.error(e, false);
    }

}
