package com.ninjabooks.error.global;

import com.ninjabooks.json.error.ErrorResponse;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class ErrorHandlerAdapter
{
    private static BodyBuilder bodyBuilder;

    /**
     * Return new instance of {@code ErrorHandleAdapter} and create {@code BodyBuilder} object
     * which is responsible of creating error message {@code ResponseEntity}.
     * If you want enable logging on handler endpoint use an appropriate chain method.
     * <p>
     * To create {@code ResponseEntity} invoke
     *
     * @param status  -  http status
     * @param request - request from Web
     * @param e       - exception to handle
     * @return new insance of ErrorHandlerAdapter
     * @see #response
     */

    public static ErrorHandlerAdapter error(HttpStatus status, HttpServletRequest request, Exception e) {
        bodyBuilder = new BodyBuilder(status, request, e);
        return new ErrorHandlerAdapter();
    }

    /**
     * Default status is bad request (400)
     *
     * @param request - request from Web
     * @param e       - exception to handle
     * @return new insance of ErrorHandlerAdapter
     */

    public static ErrorHandlerAdapter error(HttpServletRequest request, Exception e) {
        bodyBuilder = new BodyBuilder(HttpStatus.BAD_REQUEST, request, e);
        return new ErrorHandlerAdapter();
    }

    public ErrorHandlerAdapter withLogging(ErrorHandlerName handlerName) {
        bodyBuilder.withErrorLogging(handlerName);
        return this;
    }

    public ErrorHandlerAdapter withLogging() {
        bodyBuilder.withErrorLogging(ErrorHandlerName.HANDLER);
        return this;
    }

    /**
     * Enable short error logs with only exception name and short message.
     *
     * @return instance of ErrorHandlerAdapter
     */

    public ErrorHandlerAdapter withShortLogging() {
        bodyBuilder.withShortErrorLogging();
        return this;
    }

    /**
     * Enable stacktrace in logs.
     *
     * @return instance of ErrorHandlerAdapter
     */

    public ErrorHandlerAdapter withStackTrace() {
        bodyBuilder.withStackTrace();
        return this;
    }

    /**
     * As result of calling this method you should obtain JSON with follows structure
     * <p>
     * <code>
     * {
     * "status" : status_code,
     * "message" : exception_short_meesage,
     * "request" : request_link_from_web
     * }
     * </code>
     *
     * @param <E> - pseudo generic argument
     * @return RensponseEnity
     */

    @SuppressWarnings("unchecked")
    public <E> ResponseEntity<E> response() {
        return (ResponseEntity<E>) bodyBuilder.build();
    }

    private static class BodyBuilder
    {
        private static final String CLASS_NAME_CALLER = Thread.currentThread().getStackTrace()[3].getClassName();
        private static final Logger logger = LogManager.getLogger(CLASS_NAME_CALLER);

        private final HttpStatus httpStatus;
        private final HttpServletRequest request;
        private final Exception exception;

        BodyBuilder(HttpStatus httpStatus, HttpServletRequest request, Exception exception) {
            this.httpStatus = httpStatus;
            this.request = request;
            this.exception = exception;
        }

        void withErrorLogging(ErrorHandlerName errorHandlerName) {
            logger.error("{} exception occurs\n" +
                    "Request: {}\n" +
                    "Exception name: {}\n" +
                    "Exception short message: {}",
                errorHandlerName.getArgumentName(), request.getRequestURI(), exception, exception.getMessage());
        }

        void withShortErrorLogging() {
            logger.error("Exception name: {}\n" +
                    "Exception short message: {}",
                exception, exception.getMessage());
        }

        void withStackTrace() {
            StackTraceElement[] stackTrace = exception.getStackTrace();
            StringBuilder builder = new StringBuilder();
            for (StackTraceElement stackTraceElement : stackTrace) {
                builder.append(stackTraceElement).append("\n");
            }
            logger.error("Exception stacktrace:\n{}", builder);
        }

        public ResponseEntity<ErrorResponse> build() {
            ErrorResponse errorResponse = new ErrorResponse(httpStatus, exception, request.getRequestURI());
            return ResponseEntity.status(httpStatus).body(errorResponse);
        }
    }

    public enum ErrorHandlerName
    {
        PARAMETER("Parameter"),
        CONSTRAIN("Constrain violation"),
        VALID("Validation"),
        METHOD("Method argument"),
        HANDLER("Handler"),
        REQUEST("Request type"),
        MEDIA_TYPE("Media type"),
        NOT_SUPPORTED("Not supported");

        private String argumentName;

        ErrorHandlerName(String argumentName) {
            this.argumentName = argumentName;
        }

        public String getArgumentName() {
            return argumentName;
        }
    }
}
