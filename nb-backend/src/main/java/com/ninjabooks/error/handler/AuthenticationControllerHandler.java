package com.ninjabooks.error.handler;

import com.ninjabooks.security.controller.AuthenticationController;
import com.ninjabooks.error.global.ErrorHandlerAdapter;
import com.ninjabooks.json.error.ErrorResponse;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ControllerAdvice(basePackageClasses = AuthenticationController.class)
public class AuthenticationControllerHandler
{
    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<ErrorResponse> authorizationCannotPerform(HttpServletRequest request, AuthenticationException e) throws Exception {
        return ErrorHandlerAdapter.error(HttpStatus.UNAUTHORIZED, request, e).withShortLogging().response();
    }
}
