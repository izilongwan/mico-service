package com.common.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.common.entity.R;

@ControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler
    public R<Object> handler(Exception e) {
        return R.ERROR(e, e.getMessage());
    }
}
