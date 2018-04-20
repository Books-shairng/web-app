package com.ninjabooks.error.exception;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class TokenException extends RuntimeException
{
    private static final long serialVersionUID = 7304350540256557473L;

    public TokenException(String message) {
        super(message);
    }
}
