package com.ninjabooks.error.global;

import com.ninjabooks.error.global.ErrorHandlerAdapter.ErrorHandlerName;
import com.ninjabooks.json.error.ErrorResponse;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ControllerAdvice
public class GlobalRestExceptionHandler extends ResponseEntityExceptionHandler
{
    @Override
    @SuppressWarnings("unchecked")
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        ResponseEntity<ErrorResponse> response = ErrorHandlerAdapter.error(((ServletWebRequest) request).getRequest(), e)
            .withLogging(ErrorHandlerName.VALID)
            .response();
        return new ResponseEntity(response.getBody(), headers, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException e,
                                                                          HttpHeaders headers,
                                                                          HttpStatus status,
                                                                          WebRequest request) {
        return ErrorHandlerAdapter.error(((ServletWebRequest) request).getRequest(), e)
            .withLogging(ErrorHandlerName.PARAMETER)
            .response();
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException e, WebRequest request) {
        return ErrorHandlerAdapter.error(((ServletWebRequest) request).getRequest(), e)
            .withLogging(ErrorHandlerName.CONSTRAIN)
            .withStackTrace()
            .response();
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e,
                                                                          WebRequest request) {
        return ErrorHandlerAdapter.error(((ServletWebRequest) request).getRequest(), e).withLogging().response();
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException e,
                                                                   HttpHeaders headers,
                                                                   HttpStatus status,
                                                                   WebRequest request) {
        return ErrorHandlerAdapter.error(HttpStatus.ACCEPTED, ((ServletWebRequest) request).getRequest(), e)
            .withLogging()
            .response();
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                                         HttpHeaders headers,
                                                                         HttpStatus status,
                                                                         WebRequest request) {
        return ErrorHandlerAdapter.error(HttpStatus.METHOD_NOT_ALLOWED, ((ServletWebRequest) request).getRequest(), e)
            .withLogging()
            .response();
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException e,
                                                                     HttpHeaders headers,
                                                                     HttpStatus status,
                                                                     WebRequest request) {
        return ErrorHandlerAdapter.error(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ((ServletWebRequest) request).getRequest(), e)
            .withLogging()
            .response();
    }

}
