package com.common.bo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
public class ParamBo<T extends Object> {
    String name;
    T value;
    Annotation annotation;
    Field field;
    int index;
    boolean isLastIndex;

    public ParamBo(String name, T value, Annotation annotation, Field field) {
        this.name = name;
        this.value = value;
        this.annotation = annotation;
        this.field = field;
    }

    public ParamBo(String name, T value, Annotation annotation) {
        this(name, value, annotation, null);
    }
}
