package com.ninjabooks.error.handler;

import com.ninjabooks.controller.CommentController;
import com.ninjabooks.error.exception.comment.CommentException;
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
@ControllerAdvice(basePackageClasses = CommentController.class)
public class CommentControlllerHandler
{
    @ExceptionHandler(value = CommentException.class)
    public ResponseEntity<ErrorResponse> unableToAddComment(HttpServletRequest request, Exception e) throws Exception {
        return ErrorHandlerAdapter.error(request, e).withLogging().response();
    }
}
