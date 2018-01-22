package com.ninjabooks.error.handler;

import com.ninjabooks.controller.QueryController;
import com.ninjabooks.error.global.ErrorHandlerAdapter;
import com.ninjabooks.json.error.ErrorResponse;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ControllerAdvice(basePackageClasses = QueryController.class)
public class QueryControllerHandler
{
    @ExceptionHandler(value = {PersistenceException.class, IllegalStateException.class})
    public ResponseEntity<ErrorResponse> wrongSQLQueryParsing(Exception e, HttpServletRequest request) {
        return ErrorHandlerAdapter.error(request, e).withShortLogging().response();
    }
}
