package com.common.aop.anno;

public @interface ValidatorPropAnno {
    String value();

    String prop();

    String message() default "";
}
