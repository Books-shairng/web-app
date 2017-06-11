package com.ninjabooks.error.mapper;

import com.ninjabooks.controller.BookController;
import com.ninjabooks.error.QRCodeException;
import com.ninjabooks.json.error.ErrorResponse;
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
@ControllerAdvice(basePackageClasses = {BookController.class})
public class QRCodeMapper
{
    @ExceptionHandler(value = QRCodeException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> qrCodeCannotGenerate(HttpServletRequest request, QRCodeException e) throws  Exception {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                e,
                request.getRequestURI()));
    }
}
