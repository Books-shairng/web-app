package com.ninjabooks.error.exception.borrow;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class BorrowException extends Exception
{
    private static final long serialVersionUID = 8468038047139123270L;

    public BorrowException(String message) {
        super(message);
    }

    public BorrowException(String message, Throwable cause) {
        super(message, cause);
    }
}
