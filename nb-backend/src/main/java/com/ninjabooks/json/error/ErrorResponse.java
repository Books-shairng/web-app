package com.ninjabooks.json.error;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class ErrorResponse implements Serializable
{
    private static final long serialVersionUID = -3253330504028658177L;

    private int status;
    private String message;
    private String request;

    public ErrorResponse(HttpStatus status, Exception ex, String request) {
        this.status = status.value();
        this.message = ex.getMessage();
        this.request = request;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status.value();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
