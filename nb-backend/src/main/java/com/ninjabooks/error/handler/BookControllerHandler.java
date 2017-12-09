package com.ninjabooks.error.handler;

import com.ninjabooks.controller.BookController;
import com.ninjabooks.error.exception.qrcode.QRCodeException;
import com.ninjabooks.error.global.ErrorHandlerAdapter;
import com.ninjabooks.json.error.ErrorResponse;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ControllerAdvice(basePackageClasses = {BookController.class})
public class BookControllerHandler
{
    @ExceptionHandler(value = {QRCodeException.class, EntityNotFoundException.class})
    public ResponseEntity<ErrorResponse> qrCodeCannotGenerate(HttpServletRequest request, Exception e) throws Exception {
        return ErrorHandlerAdapter.error(request, e).withLogging().response();
    }
}
