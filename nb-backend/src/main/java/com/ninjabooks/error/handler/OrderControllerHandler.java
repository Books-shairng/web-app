package com.ninjabooks.error.handler;

import com.ninjabooks.error.exception.order.OrderException;
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
@ControllerAdvice
public class OrderControllerHandler
{
    @ExceptionHandler(value = OrderException.class)
    public ResponseEntity<ErrorResponse> unableToCreateQueue(HttpServletRequest request,
                                                             OrderException e) throws Exception {
        return ErrorHandlerAdapter.error(request, e).withLogging().response();
    }

}
