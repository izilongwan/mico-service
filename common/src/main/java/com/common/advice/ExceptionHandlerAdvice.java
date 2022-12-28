package com.common.advice;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.common.entity.R;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler
    public R<Object> handler(Exception e) {
        return R.ERROR(e, e.getMessage());
    }
}
