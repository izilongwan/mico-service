package com.common.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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

    String query = null;

    long timecost = 0;

    long timestamp = System.currentTimeMillis();

    public R() {
        initPath();
    }

    public R(T data) {
        this();
        this.data = data;
    }

    public R(T data, String message) {
        this(data);
        this.message = message;
    }

    public R(T data, Integer code, String message) {
        this(data, message);
        this.code = code;
    }

    public R(T data, Integer code, String message, long timecost) {
        this(data, code, message);
        this.timecost = timecost;
    }

    private void initPath() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();

        if (Objects.isNull(attrs)) {
            return;
        }

        ServletRequestAttributes reqAttrs = (ServletRequestAttributes) attrs;

        if (Objects.isNull(reqAttrs)) {
            return;
        }

        HttpServletRequest request = reqAttrs.getRequest();

        this.path = request.getRequestURI();
        this.query = request.getQueryString();
    }
}
