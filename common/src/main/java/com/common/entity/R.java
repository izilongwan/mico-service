package com.common.entity;

import java.io.Serializable;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class R<T> implements Serializable {
    final public static <T> R<T> AUTO(T code) {
        if (Objects.equals(code, 0)) {
            return R.SUCCESS(code);
        }

        return R.ERROR(code);
    }

    final public static <T> R<T> SUCCESS() {
        return new R<T>();
    }

    final public static <T> R<T> SUCCESS(T data) {
        return new R<T>(data);
    }

    final public static <T> R<T> SUCCESS(T data, long timecost) {
        return new R<T>(data, timecost);
    }

    final public static <T> R<T> SUCCESS(T data, String message) {
        return new R<T>(data, RCM.SUCCESS_CODE, message);
    }

    final public static <T> R<T> SUCCESS(T data, String message, long timecost) {
        return new R<T>(data, RCM.SUCCESS_CODE, message, timecost);
    }

    final public static <T> R<T> ERROR() {
        return new R<T>(null, RCM.ERROR_CODE, RCM.ERROR_MESSAGE);
    }

    final public static <T> R<T> ERROR(T data) {
        return new R<T>(data, RCM.ERROR_CODE, RCM.ERROR_MESSAGE);
    }

    final public static <T> R<T> ERROR(String message) {
        return new R<T>(null, RCM.ERROR_CODE, message);
    }

    final public static <T> R<T> ERROR(T data, String message) {
        return new R<T>(data, RCM.ERROR_CODE, message);
    }

    final public static <T> R<T> ERROR(String message, long timecost) {
        return new R<T>(null, RCM.ERROR_CODE, message, timecost);
    }

    final public static <T> R<T> ERROR(T data, String message, long timecost) {
        return new R<T>(data, RCM.ERROR_CODE, message, timecost);
    }

    Integer code = RCM.SUCCESS_CODE;

    T data = null;

    String message = RCM.SUCCESS_MESSAGE;

    String path = null;

    long timecost = 0;

    long timestamp = System.currentTimeMillis();

    public R() {
    }

    public R(T data) {
        this.data = data;
    }

    public R(T data, long timecost) {
        this.data = data;
        this.timecost = timecost;
    }

    public R(T data, Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public R(T data, Integer code, String message, long timecost) {
        this.code = code;
        this.message = message;
        this.timecost = timecost;
    }
}
