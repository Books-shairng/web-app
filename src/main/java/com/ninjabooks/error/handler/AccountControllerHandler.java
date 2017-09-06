package com.ninjabooks.error.handler;

import com.ninjabooks.controller.AccountController;
import com.ninjabooks.error.user.UserAlreadyExistException;
import com.ninjabooks.json.error.ErrorResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ControllerAdvice(basePackageClasses = AccountController.class)
public class AccountControllerHandler
{
    private static final Logger Logger = LogManager.getLogger(AccountController.class);

    @ExceptionHandler(value = UserAlreadyExistException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> userAlreadyExist(UserAlreadyExistException e, HttpServletRequest request) {
        Logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                e,
                request.getRequestURI()));
    }
}
