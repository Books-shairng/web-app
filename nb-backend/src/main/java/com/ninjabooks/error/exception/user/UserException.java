package com.ninjabooks.error.exception.user;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class UserException extends Exception
{
    private static final long serialVersionUID = 4812684653468513783L;

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }
}
