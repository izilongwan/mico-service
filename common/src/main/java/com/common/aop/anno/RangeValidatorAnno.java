package com.common.aop.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RangeValidatorAnno {
    long min() default Long.MIN_VALUE;

    long max() default Long.MAX_VALUE;

    long value() default 0;
}
