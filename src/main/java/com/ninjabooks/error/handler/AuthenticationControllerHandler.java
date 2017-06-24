package com.ninjabooks.error.handler;

import com.ninjabooks.controller.AuthenticationController;
import com.ninjabooks.json.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ControllerAdvice(basePackageClasses = AuthenticationController.class)
public class AuthenticationControllerHandler
{
    @ExceptionHandler(value = AuthenticationException.class)
    @ResponseStatus(value= HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> authorizationCannotPerform(HttpServletRequest request, AuthenticationException e) throws Exception {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new ErrorResponse(
                HttpStatus.UNAUTHORIZED,
                e,
                request.getRequestURI()));
    }
}
