package com.ninjabooks.error.borrow;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class BorrowMaximumLimitException extends BorrowException
{
    public BorrowMaximumLimitException(String message) {
        super(message);
    }

    public BorrowMaximumLimitException(String message, Throwable cause) {
        super(message, cause);
    }
}
