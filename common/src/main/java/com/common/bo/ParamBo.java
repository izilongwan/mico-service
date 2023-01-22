package com.common.bo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Data
@ToString
@Accessors(chain = true)
public class ParamBo {
    String name;
    Object value;
    Annotation annotation;
    int index;
    boolean isLastIndex;
    Field field;

    public ParamBo(String name, Object value, Annotation annotation, int index, boolean isLastIndex) {
        this.name = name;
        this.value = value;
        this.annotation = annotation;
        this.index = index;
        this.isLastIndex = isLastIndex;
    }
}
