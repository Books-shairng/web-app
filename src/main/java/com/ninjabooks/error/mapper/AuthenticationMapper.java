package com.ninjabooks.error.mapper;

import com.ninjabooks.controller.AuthenticationController;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ControllerAdvice(basePackageClasses = AuthenticationController.class)
public class AuthenticationMapper
{
    @ExceptionHandler(value = AuthenticationException.class)
    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    public void authorizationCannotPerform(HttpServletResponse response, AuthenticationException e) throws Exception {
        response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }
}
