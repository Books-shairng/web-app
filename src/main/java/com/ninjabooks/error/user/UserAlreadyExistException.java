package com.ninjabooks.error.user;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class UserAlreadyExistException extends UserException
{
    public UserAlreadyExistException(String message) {
        super(message);
    }

    public UserAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
