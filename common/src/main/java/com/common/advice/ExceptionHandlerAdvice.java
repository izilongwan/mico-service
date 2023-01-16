package com.common.advice;

import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.common.entity.R;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler
    public R<Object> handler(BadSqlGrammarException e) {
        return R.ERROR(e, e.getCause().toString());
    }

    @ExceptionHandler
    public R<Object> handler(Exception e) {
        return R.ERROR(e, e.getMessage());
    }
}
