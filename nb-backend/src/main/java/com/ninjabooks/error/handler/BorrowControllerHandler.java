package com.ninjabooks.error.handler;

import com.ninjabooks.controller.BorrowController;
import com.ninjabooks.error.exception.borrow.BorrowException;
import com.ninjabooks.error.exception.qrcode.QRCodeException;
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
@ControllerAdvice(basePackageClasses = BorrowController.class)
public class BorrowControllerHandler
{
    @ExceptionHandler(value = {BorrowException.class, QRCodeException.class})
    public ResponseEntity<ErrorResponse> incorrectBorrow(HttpServletRequest request, Exception e) throws Exception {
        return ErrorHandlerAdapter.error(request, e).withLogging().withStackTrace().response();
    }
}
