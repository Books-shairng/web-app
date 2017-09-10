package com.ninjabooks.error.order;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class OrderException extends Exception
{
    public OrderException(String message) {
        super(message);
    }

    public OrderException(String message, Throwable cause) {
        super(message, cause);
    }
}
