package com.common.aop.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

@Documented
@Target({ ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD, ElementType.LOCAL_VARIABLE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidatorAnno {
    @AliasFor("pattern")
    String value() default "";

    @AliasFor("value")
    String pattern() default "";

    String message() default "";

    ValidatorPropAnno[] values() default {};
}
