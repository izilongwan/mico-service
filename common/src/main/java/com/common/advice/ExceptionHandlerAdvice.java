package com.common.advice;

import java.io.FileNotFoundException;
import java.net.BindException;
import java.security.acl.NotOwnerException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.jar.JarException;

import javax.naming.InsufficientResourcesException;

import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.common.entity.R;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    final private static List<Class<? extends Throwable>> classList = new ArrayList<Class<? extends Throwable>>() {
        {
            add(BadSqlGrammarException.class);
            add(FileNotFoundException.class);
            add(JarException.class);
            add(MissingResourceException.class);
            add(NotOwnerException.class);
            add(ConcurrentModificationException.class);
            add(InsufficientResourcesException.class);
            add(BindException.class);
            add(OutOfMemoryError.class);
            add(StackOverflowError.class);
            add(SQLException.class);
        }
    };

    @ExceptionHandler
    public R<Exception> handler(Exception e) {
        return error(e);
    }

    public static R<Exception> error(Exception e, boolean isSecurityMessage) {
        return isSecurityMessage
                ? R.ERROR(e, e.getMessage())
                : R.ERROR(null,
                        Objects.nonNull(e.getCause())
                                ? e.getCause().getMessage()
                                : e.getMessage());
    }

    private static R<Exception> error(Exception e) {
        boolean isSecurity = classList.indexOf(e.getClass()) >= 0;
        return error(e, isSecurity);
    }
}
