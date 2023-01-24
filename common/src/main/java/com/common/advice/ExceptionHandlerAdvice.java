package com.common.advice;

import java.util.Objects;

import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.common.entity.R;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler
    public R<Object> handler(BadSqlGrammarException e) {
        return Error(e);
    }

    @ExceptionHandler
    public R<Object> handler(Exception e) {
        return Error(e);
    }

    private R<Object> Error(Exception e) {
        if (Objects.nonNull(e) && Objects.nonNull(e.getCause())) {
            Object data = Objects.equals(e.getCause().getMessage(), "AOP")
                    ? null
                    : e;
            return R.ERROR(data, e.getMessage());
        }

        return R.ERROR(e, e.getMessage());
    }
}
