package com.ninjabooks.error.mapper;

import com.ninjabooks.controller.UserController;
import com.ninjabooks.error.UserAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ControllerAdvice(basePackageClasses = UserController.class)
public class UserAlreadyExistMapper
{
    @ExceptionHandler(value = UserAlreadyExistException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Username already exist in database")
    public void userAlreadyExist() {}
}
