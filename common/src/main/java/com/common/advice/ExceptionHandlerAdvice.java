package com.common.advice;

import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.common.entity.R;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler
    public R<Exception> handler(BadSqlGrammarException e) {
        return Error(e);
    }

    @ExceptionHandler
    public R<Exception> handler(Exception e) {
        return Error(e);
    }

    private R<Exception> Error(Exception e) {
        return R.ERROR(e, e.getMessage());
    }
}
