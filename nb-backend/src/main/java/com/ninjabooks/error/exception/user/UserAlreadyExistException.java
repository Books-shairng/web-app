package com.ninjabooks.error.exception.user;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class UserAlreadyExistException extends UserException
{
    private static final long serialVersionUID = 6697390192071271641L;

    public UserAlreadyExistException(String message) {
        super(message);
    }

    public UserAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
