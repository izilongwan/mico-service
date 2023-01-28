package com.common.bo;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
public class ParamBo<T extends Object> {
    String name;
    T value;
    Parameter parameter;
    Field field;

    public ParamBo(String name, T value, Parameter parameter, Field field) {
        this.name = name;
        this.value = value;
        this.parameter = parameter;
        this.field = field;
    }

    public ParamBo(String name, T value, Parameter parameter) {
        this(name, value, parameter, null);
    }
}
