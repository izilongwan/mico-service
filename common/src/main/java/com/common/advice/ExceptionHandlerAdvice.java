package com.common.advice;

import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.common.entity.R;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler
    public R<Exception> handler(BadSqlGrammarException e) {
        return Error(e, true);
    }

    @ExceptionHandler
    public R<Exception> handler(Exception e) {
        return Error(e);
    }

    private R<Exception> Error(Exception e, boolean b) {
        return R.ERROR(e, e.getCause().getMessage());
    }

    private R<Exception> Error(Exception e) {
        return R.ERROR(e, e.getMessage());
    }
}
