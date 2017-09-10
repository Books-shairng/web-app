package com.ninjabooks.error.user;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class UserException extends Exception
{
    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }
}
