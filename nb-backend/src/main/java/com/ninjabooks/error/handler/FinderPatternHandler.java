package com.ninjabooks.error.handler;

import com.ninjabooks.controller.AccountController;
import com.ninjabooks.security.controller.AuthenticationController;
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
@ControllerAdvice(basePackageClasses = {AccountController.class, AuthenticationController.class})
public class FinderPatternHandler
{
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> wrongSecurityPattern(HttpServletRequest request,
                                                              IllegalArgumentException e) throws Exception {
        return ErrorHandlerAdapter.error(request, e).withShortLogging().response();
    }
}
