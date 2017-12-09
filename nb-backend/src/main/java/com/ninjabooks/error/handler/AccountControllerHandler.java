package com.ninjabooks.error.handler;

import com.ninjabooks.controller.AccountController;
import com.ninjabooks.error.exception.user.UserAlreadyExistException;
import com.ninjabooks.error.global.ErrorHandlerAdapter;
import com.ninjabooks.json.error.ErrorResponse;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ControllerAdvice(basePackageClasses = AccountController.class)
public class AccountControllerHandler
{
    @ExceptionHandler(value = UserAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> userAlreadyExist(UserAlreadyExistException e, HttpServletRequest request) {
        return ErrorHandlerAdapter.error(request, e).withLogging().response();
    }
}
