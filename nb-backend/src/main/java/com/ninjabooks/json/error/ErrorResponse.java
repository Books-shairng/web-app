package com.ninjabooks.json.error;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class ErrorResponse implements Serializable
{
    private static final long serialVersionUID = -3253330504028658177L;

    private final int status;
    private final String message;
    private final String request;

    @JsonCreator
    public ErrorResponse(@JsonProperty(value = "status") HttpStatus status,
                         @JsonProperty(value = "message") Exception ex,
                         @JsonProperty(value = "request") String request) {
        this.status = status.value();
        this.message = ex.getMessage();
        this.request = request;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getRequest() {
        return request;
    }

}
