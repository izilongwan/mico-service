package com.common.aop;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Locale;
import java.util.Objects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

import com.common.aop.anno.ValidatorAnno;
import com.common.aop.anno.ValidatorPropAnno;
import com.common.bo.ParamBo;
import com.common.entity.R;
import com.common.util.AopCheckUtil;

@Configuration
@Aspect
@SuppressWarnings("unchecked")
public class ValidatorAop {
    @Pointcut("execution(public * com..controller.*Controller.*(..))")
    private void range() {

    }

    @Around("range() || anno()")
    private Object aroundCheckMethodParam(ProceedingJoinPoint proceedingJoinPoint)
            throws Throwable {
        try {
            AopCheckUtil.checkMethodParam(
                    proceedingJoinPoint,
                    paramBo -> {
                        Parameter parameter = paramBo.getParameter();

                        if (Objects.isNull(parameter)) {
                            return null;
                        }

                        if (parameter.isAnnotationPresent(ValidatorAnno.class)) {
                            ValidatorAnno annotation = parameter.getAnnotation(ValidatorAnno.class);
                            String checkValid = checkValid(annotation, paramBo);
                            AopCheckUtil.throwException(checkValid);
                        }

                        return null;
                    });
        } catch (Exception e) {
            return R.ERROR(e.getMessage());
        }

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
                        Field field = paramBo.getField();

                        if (Objects.isNull(field)) {
                            return null;
                        }

                        if (field.isAnnotationPresent(ValidatorAnno.class)) {
                            ValidatorAnno annotation = field.getAnnotation(ValidatorAnno.class);
                            String checkValid = checkValid((ValidatorAnno) annotation, paramBo);
                            AopCheckUtil.throwException(checkValid);
                        }

                        return null;
                    });
            return proceedingJoinPoint.proceed();

        } catch (Throwable e) {
            return R.ERROR(e.getMessage());
        }

    }

    private <T> String checkValid(ValidatorAnno validatorAnno, ParamBo<T> paramBo) {
        T val = paramBo.getValue();
        String name = paramBo.getName();

        if (Objects.isNull(val)) {
            return String.format("属性[%s]的值为空", name);
        }

        Class<T> clazz = (Class<T>) val.getClass();
        ValidatorPropAnno[] propAnnos = validatorAnno.values();

        if (propAnnos.length > 0) {
            String checkPropAnno = checkPropAnno(propAnnos, clazz, val);
            return Objects.isNull(checkPropAnno) ? null : checkPropAnno;
        }

        String regexp = validatorAnno.value();

        if (regexp.isEmpty()) {
            regexp = validatorAnno.regexp();
        }

        if (regexp.isEmpty()) {
            return null;
        }

        String value = String.valueOf(val);

        if (!value.matches(regexp)) {
            String message = validatorAnno.message();
            String simpleName = "";

            Field field = paramBo.getField();

            if (Objects.nonNull(field)) {
                simpleName = field.getDeclaringClass().getSimpleName() + ".";
            }

            return message.isEmpty()
                    ? String.format("属性[%s%s]的值不匹配", simpleName, name)
                    : message;
        }

        return null;
    }

    private <T> String checkPropAnno(ValidatorPropAnno[] propAnnos, Class<T> clazz, T val) {
        for (ValidatorPropAnno propAnno : propAnnos) {
            try {
                Method method = clazz.getDeclaredMethod(getMethodName(propAnno.prop()));
                method.setAccessible(true);
                Object invoke = method.invoke(val);
                String value = String.valueOf(invoke);
                String simpleName = method.getDeclaringClass().getSimpleName();

                if (!value.matches(propAnno.value())) {
                    String message = propAnno.message();
                    return message.isEmpty()
                            ? String.format("属性[%s.%s]的值不匹配", simpleName, propAnno.prop())
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
