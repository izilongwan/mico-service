package com.common.aop.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestLimitAnno {
    int value() default 60;

    long time() default 1000 * 60;

    String keyPrefix() default "RequestLimitAnno";
}
