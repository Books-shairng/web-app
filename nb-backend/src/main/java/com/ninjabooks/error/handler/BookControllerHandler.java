package com.ninjabooks.error.handler;

import com.ninjabooks.controller.BookController;
import com.ninjabooks.error.exception.qrcode.QRCodeException;
import com.ninjabooks.json.error.ErrorResponse;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ControllerAdvice(basePackageClasses = {BookController.class})
public class BookControllerHandler
{
    @ExceptionHandler(value = {QRCodeException.class, EntityNotFoundException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> qrCodeCannotGenerate(HttpServletRequest request, Exception e) throws Exception {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                e,
                request.getRequestURI()));
    }
}
