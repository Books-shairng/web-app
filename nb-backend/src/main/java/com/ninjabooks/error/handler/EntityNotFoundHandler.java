package com.ninjabooks.error.handler;

import com.ninjabooks.error.global.ErrorHandlerAdapter;
import com.ninjabooks.json.error.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ControllerAdvice(basePackages = "com.ninjabooks.controller")
public class EntityNotFoundHandler
{
    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> enitityNotFound(HttpServletRequest request,
                                                         EntityNotFoundException e) throws Exception {
        return ErrorHandlerAdapter.error(request, e).withShortLogging().response();
    }
}
