package com.ninjabooks.error.borrow;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class BorrowException extends Exception
{
    public BorrowException(String message) {
        super(message);
    }

    public BorrowException(String message, Throwable cause) {
        super(message, cause);
    }
}
