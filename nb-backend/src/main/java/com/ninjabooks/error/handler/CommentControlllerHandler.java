package com.ninjabooks.error.handler;

import com.ninjabooks.controller.BorrowController;
import com.ninjabooks.controller.CommentController;
import com.ninjabooks.error.exception.comment.CommentException;
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
@ControllerAdvice(basePackageClasses = CommentController.class)
public class CommentControlllerHandler
{
    private static final Logger logger = LogManager.getLogger(BorrowController.class);

    @ExceptionHandler(value = CommentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> unableToAddComment(HttpServletRequest request, Exception e) throws  Exception {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                e,
                request.getRequestURI()));
    }
}
