package com.ninjabooks.error.handler;

import com.ninjabooks.controller.AccountMgmtController;
import com.ninjabooks.error.exception.management.ManagementException;
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
@ControllerAdvice(basePackageClasses = AccountMgmtController.class)
public class AccountMgmtControllerHandler
{
    @ExceptionHandler(value = ManagementException.class)
    public ResponseEntity<ErrorResponse> passworNotUnique(ManagementException e, HttpServletRequest request) {
        return ErrorHandlerAdapter.error(request, e).withLogging().response();
    }
}
