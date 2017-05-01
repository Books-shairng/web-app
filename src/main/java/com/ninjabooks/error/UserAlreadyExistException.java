package com.ninjabooks.error;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class UserAlreadyExistException extends RuntimeException
{
    public UserAlreadyExistException(String message) {
        super(message);
    }

    public UserAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
