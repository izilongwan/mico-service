package com.dianping.advice;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.common.entity.R;

@RestControllerAdvice
public class WebExceptionAdvice {
    @ExceptionHandler
    public R<Object> exceptionHandler(Exception e) {
        return R.ERROR(e.getMessage());
    }
}
